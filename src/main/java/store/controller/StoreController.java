package store.controller;

import static store.common.constant.PromotionNotice.GET_FREE_M_NOTICE;
import static store.common.constant.PromotionNotice.OUT_OF_STOCK_NOTICE;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import store.common.dto.PromotionConditionResult;
import store.common.dto.PromotionResult;
import store.common.dto.PurchaseRequest;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Cart;
import store.model.Product;
import store.model.ProductCatalog;
import store.model.Products;
import store.model.PromotionCatalog;
import store.model.Receipt;
import store.view.OutputView;

public class StoreController {

    private final InputHandler inputHandler;
    private final OutputView outputView;

    private final ProductCatalog productCatalog;
    private final PromotionCatalog promotionCatalog;

    public StoreController(InputHandler inputHandler, OutputView outputView) {
        this.inputHandler = inputHandler;
        this.outputView = outputView;
        promotionCatalog = inputHandler.getPromotions();
        productCatalog = inputHandler.getProducts(promotionCatalog);
    }

    public void run() {
        PurchaseRequest purchaseItems = getPurchaseRequest();
        Cart cart = Cart.empty();

        Products promotionalProducts = getPromotionalProducts(purchaseItems.getProductNames());
        List<PromotionResult> promotionResults = addPromotionalProductToCart(promotionalProducts, purchaseItems, cart);
        addRegularProductToCart(purchaseItems, cart);
        int membershipDiscount = applyMembership(cart, promotionalProducts);
        purchaseProducts(cart);
        printReceipt(cart, promotionResults, membershipDiscount);
        repurchaseIfWant();
    }

    private PurchaseRequest getPurchaseRequest() {
        outputView.printStoreInventory(productCatalog);
        return inputHandler.getPurchaseItems(productCatalog);
    }

    private void addRegularProductToCart(PurchaseRequest purchaseItems, Cart cart) {
        purchaseItems.items().keySet().forEach(productName -> {
            Product product = productCatalog.getProductByName(productName).products().stream()
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            cart.addProduct(product, purchaseItems.items().get(productName));
        });
    }

    private List<PromotionResult> addPromotionalProductToCart(
            Products promotionalProducts, PurchaseRequest purchaseItems, Cart cart
    ) {
        promotionalProducts.products().forEach(product -> {
            int purchaseQuantity = purchaseItems.items().get(product.name());
            PromotionConditionResult conditionResult = product.checkPromotionCondition(purchaseQuantity);
            handlePromotionCondition(purchaseItems, cart, product, conditionResult);
        });
        return applyPromotion(cart);
    }

    private Products getPromotionalProducts(PurchaseProductNames productNames) {
        return Products.from(
                productCatalog.getProductsByNames(productNames.productNames()).products().stream()
                        .filter(Product::isPromotionApplicable)
                        .collect(Collectors.toSet())
        );
    }

    private List<PromotionResult> applyPromotion(Cart cart) {
        return cart.cart().keySet().stream()
                .filter(Product::isPromotionApplicable)
                .map(product -> product.applyPromotion(cart.cart().get(product)))
                .toList();
    }

    private void handlePromotionCondition(
            PurchaseRequest purchaseItems, Cart cart, Product product, PromotionConditionResult conditionResult
    ) {
        if (GET_FREE_M_NOTICE.matches(conditionResult.message())) {
            handleGetFreeCondition(purchaseItems, cart, product, conditionResult);
            return;
        }
        if (OUT_OF_STOCK_NOTICE.matches(conditionResult.message())) {
            handleOutOfPromotionStock(purchaseItems, cart, product, conditionResult);
            return;
        }
        cart.addProduct(product, conditionResult.stockUsed());
        purchaseItems.items()
                .replace(product.name(), purchaseItems.items().get(product.name()) - conditionResult.stockUsed());
    }

    private void handleOutOfPromotionStock(
            PurchaseRequest purchaseItems, Cart cart, Product product, PromotionConditionResult conditionResult
    ) {
        int purchaseQuantity = purchaseItems.items().get(product.name());
        outputView.printPromotionOutOfStockNotice(conditionResult.message());
        cart.addProduct(product, conditionResult.stockUsed());
        if (inputHandler.isAffirmative()) {
            purchaseItems.items().replace(product.name(), purchaseQuantity - conditionResult.stockUsed());
            return;
        }
        purchaseItems.items().remove(product.name());
    }

    private void handleGetFreeCondition(
            PurchaseRequest purchaseItems, Cart cart, Product product, PromotionConditionResult conditionResult
    ) {
        int purchaseQuantity = purchaseItems.items().get(product.name());
        outputView.printGetFreeNotice(conditionResult.message());
        if (inputHandler.isAffirmative()) {
            cart.addProduct(product, purchaseQuantity + 1);
            purchaseItems.items().remove(product.name());
            return;
        }
        cart.addProduct(product, purchaseQuantity);
        purchaseItems.items().remove(product.name());
    }

    private int applyMembership(Cart cart, Products promotionalProducts) {
        outputView.printMembershipNotice();
        AtomicInteger discountAmount = new AtomicInteger();
        if (inputHandler.isAffirmative()) {
            cart.cart().forEach((product, quantity) -> {
                if (promotionalProducts.products().contains(product)) {
                    return;
                }
                discountAmount.addAndGet(product.price() * quantity);
            });
        }
        discountAmount.updateAndGet(value -> Math.min(value * 30 / 100, 8000));
        return discountAmount.get();
    }

    private static void purchaseProducts(Cart cart) {
        cart.cart().forEach(Product::reduceStock);
    }

    private void printReceipt(Cart cart, List<PromotionResult> promotionResults, int membershipDiscount) {
        Receipt receipt = Receipt.of(cart, promotionResults, membershipDiscount);
        outputView.printReceipt(receipt);
    }

    private void repurchaseIfWant() {
        outputView.printRepurchaseNotice();
        if (inputHandler.isAffirmative()) {
            run();
        }
    }
}
