# java-convenience-store-precourse

# 편의점

> 구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 프로그램

## 🚀 기능 목록

> 입력의 앞뒤 공백은 제거한다.

- [x] **[입력]** 상품 목록과 행사 목록 파일을 불러온다.
    - `src/main/resources/products.md`와 `src/main/resources/promotions.md` 파일을 이용한다.
- [x] **[출력]** 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 재고가 0이라면 `재고 없음`을 출력한다.


- [x] **[입력]** 구매할 상품과 수량을 입력 받는다.
    - 상품명과 수량은 하이픈(`-`)으로, 개별 상품은 대괄호(`[]`)로 묶어 쉼표(`,`)로 구분한다.


- [ ] **[재고 관리]** 각 상품이 존재하는지 확인한다.
- [ ] **[재고 관리]** 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.


- [x] **[프로모션 할인]** 오늘 날짜와 상품 종류에 따라 프로모션 할인 가능 여부를 결정한다.
- [ ] **[프로모션 할인]** 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
- [x] **[프로모션 할인]** 프로모션은 각 지정 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.


- [ ] **[재고 관리]** 프로모션 재고 수량을 확인한다.
    - 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
- [ ] **[재고 관리]** 프로모션 기간 중에는 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족한 경우에는 일반 재고를 사용한다.


- [ ] **[출력]** 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 결제에 대한 안내 메시지를 출력한다.
- [ ] **[입력]** 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
    - 사용자가 `Y`를 입력하면 정가로 결제하고, `N`을 입력하면 해당 수량을 제외한다.


- [ ] **[프로모션 할인]** 프로모션 할인 상품에 대해 조건에 따라 적용 가능 여부를 확인한다.
- [ ] **[출력]** 프로모션 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 해택에 대한 안내 메시지를 출력한다.
- [ ] **[입력]** 프로모션 상품 수량 추가 여부를 입력받는다.
    - 사용자가 `Y`를 입력하면 상품을 추가하고, `N`을 입력하면 추가하지 않는다.


- [ ] **[출력]** 멤버십 할인 적용 여부를 확인하기 위한 안내 문구를 출력한다.
- [ ] **[입력]** 멤버십 할인 적용 여부를 입력 받는다.
    - 사용자가 `Y`를 입력하면 멤버십 할인을 적용하고 `N`을 입력하면 적용하지 않는다.


- [ ] **[멤버십 할인]** 멤버십 회원은 프로모션 미적용 금액의 30% 를 할인받는다.
- [ ] **[멤버십 할인]** 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
- [ ] **[멤버십 할인]** 멤버십 할인의 최대 한도는 8000원이다.


- [ ] **[재고 관리]** 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감한다.


- [ ] **[영수증]** 영수증 항목은 다음과 같다.
    - 총 구매액은 상품별 가격과 수량을 곱하여 계산한다.
    - 최종 결제 금액은 프로모션 및 멤버십 할인 정책을 반영하여 계산한다.
  ```text
  구매 상품 내역: 구매한 상품명, 수량, 가격
  증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
  금액 정보:
    - 총구매액: 구매한 상품의 총 수량과 총 금액
    - 행사할인: 프로모션에 의해 할인된 금액
    - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
    - 내실돈: 최종 결제 금액
  ```


- [ ] **[출력]** 영수증을 출력한다.


- [ ] **[출력]** 추가 구매 여부 확인을 위한 안내 문구를 출력한다.
- [ ] **[입력]** 추가 구매 여부를 입력 받는다.
    - 사용자가 `Y`를 입력하면 추가 구매를 진행하고, `N`을 입력하면 구매를 종료한다.

## ⚠️ 예외 사항

> `IllegalArgumentException`을 발생시키고, `[ERROR]`로 시작하는 에러 메시지를 출력 후
> 그 부분부터 입력을 다시 받는다.

- [ ] 구매할 상품과 수량 형식이 올바르지 않은 경우
    - 상품명이나 수량이 입력되지 않은 경우(빈 문자열 포함)
    - 정해진 구분자로 구분할 수 없는 경우
    - 상품 수량이 양수가 아닌 경우
- [ ] 존재하지 않는 상품을 입력한 경우
- [ ] 구매 수량이 재고 수량을 초과한 경우
- [ ] 여부를 묻는 입력에 `Y`, `N` 외의 값이 입력된 경우

## 🪧 프로그램 실행 예시

```text
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-3],[에너지바-5]

멤버십 할인을 받으시겠습니까? (Y/N)
Y 

==============W 편의점================
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
=============증	정===============
콜라		1
====================================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 7개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-10]

현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
N

==============W 편의점================
상품명		수량	금액
콜라		10 	10,000
=============증	정===============
콜라		2
====================================
총구매액		10	10,000
행사할인			-2,000
멤버십할인			-0
내실돈			 8,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 재고 없음 탄산2+1
- 콜라 1,000원 7개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[오렌지주스-1]

현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
Y

==============W 편의점================
상품명		수량	금액
오렌지주스		2 	3,600
=============증	정===============
오렌지주스		1
====================================
총구매액		2	3,600
행사할인			-1,800
멤버십할인			-0
내실돈			 1,800

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
N
```