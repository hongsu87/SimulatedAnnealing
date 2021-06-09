# SimulatedAnnealing
## 모의 담금질 구현
### 3~4차 함수의 전역 최적점 찾기
초기온도(t) : 1000, 냉각률(c) : 0.95, 자유롭게 탐색할 확률(p) : e^(-d/t)    
종료조건(반복 횟수) : t가 0.01이 될 때까지, T가 작아질수록 kt가 커지도록 설정했을 때,    
f(x) = 3x^4 - 8x^3 - 6x^2 + 24x + 9 함수의 전역 최적해를 찾을 수 있었다.    

<img src = "https://user-images.githubusercontent.com/81207799/120917674-748ddb00-c6eb-11eb-911e-70ccc365357a.png" height = "150px" width = "150px"> ![2](https://user-images.githubusercontent.com/81207799/120917487-8e7aee00-c6ea-11eb-8fdd-72c17d84b38f.png)

## 모의 담금질을 이용한 회귀선 구하기
### 1. 데이터 수집
<몸무게에 따른 키>
- 독립변수(x) : 몸무게
- 종속변수(y) : 키

  | 몸무게 | 24   | 27   | 31   | 35   | 41   | 46   | 51   | 53   | 61   | 65   |
  | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
  | 키 | 121   | 127   | 133   | 139   | 146   | 152   | 157   | 160   | 167   | 171   |

### 2. curve fitting을 위한 모델 설정
몸무게와 키는 대체로 비례하기 때문에 y = ax + b의 선형적인 모델을 선택했다.

### 3. 모의담금질 기법을 이용하여 a, b 추정
error(회귀선상의 값과 실제 데이터 값의 차이)가 최소가 되는 a와 b값을 구하면 된다. 
모의 담금질 기법을 이용하여 MSE(mean squared error)의 최소값과 이 최소값을 만족하게 하는 a와 b를 구했다.
원래 MSE는 error의 제곱의 평균값을 말하지만 최소값만 구하면 되기 때문에 코드에서는 평균을 내지 않고 구했다. 
또한, MSE의 최소값을 구하는 과정은 2차 함수의 최소값을 구하는 과정이므로 전역 최적해를 찾을 필요가 없다.
그렇기 때문에 해가 항상 좋은 쪽으로 이동하도록 설정하면 정확한 해를 얻을 수 있었다. 
#### - 결과    
위의 데이터를 입력으로 넣은 후, a와 b값을 구해본 결과 다음과 같이 나왔다.    

![3](https://user-images.githubusercontent.com/81207799/120917503-a18dbe00-c6ea-11eb-9083-1ee4cba8ba7e.png)


## 결과
- 회귀선과 데이터값을 나타낸 그래프     

![4](https://user-images.githubusercontent.com/81207799/120917523-b2d6ca80-c6ea-11eb-80e5-d4acce5efb99.png)

### 검증  
: 모의담금질로 구한 회귀선에 임의의 값들(독립변수)을 대입한 후, 정규분포 난수로 그 주위의 값들을 생성했다.
이때 생성된 데이터값들을 입력으로 넣은 결과와 실제 데이터값으로 나온 결과를 비교해보았다.

#### - 코드
```java
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(1000, 0.95);
        Random r = new Random();

        int[] x2 = new int[5];
        int[] y2 = new int[5];

        System.out.println("몸무게");
        for (int i = 0; i < 5; i++) {
            x2[i] = (i + 1) * 10 + 10;
            System.out.printf("%d ", x2[i]);
        }
        System.out.println();

        System.out.println("키");
        for (int i = 0; i < 5; i++) {
            y2[i] = (int) (1 * x2[i] + 104 + r.nextGaussian());
            System.out.printf("%d ", y2[i]);
        }
        System.out.println();

        int mse_value = sa.solve((a, b, x1, y1) -> {
            int mse = 0;
            for (int i = 0; i < x1.length; i++) {
                mse += Math.pow((a * x2[i] + b) - y2[i], 2);
            }
            return mse;
        }, x2, y2);

        System.out.println("회귀선 : " + sa.a0 + "x+" + sa.b0);
        System.out.println("MSE : " + mse_value);

    }
}
```
#### - 실행 결과
a가 1, b가 104로 실제 데이터값을 넣었을 때와 같게 나왔다. MSE값은 입력한 데이터가 회귀선 주위의 값들이라 아주 작게 나왔다.   

![5](https://user-images.githubusercontent.com/81207799/120917507-a5b9db80-c6ea-11eb-8b3e-ef259ddc8966.png)

### 결론
- 사람의 몸무게와 키는 y = x + 104의 관계식으로 설명할 수 있고, 데이터의 개수가 많아진다면 더 정확한 관계식을 얻을 수 있을 것이다. 

3, 4차 함수에서 SimulatedAnnealing으로 전역 최적해를 찾았기 때문에 2차 함수에서도 최적해를 찾을 수 있을 것이라고 생각했다. 
하지만 이 SimulatedAnnealing으로 회귀선을 구했을 때, 항상 최적해가 나오지는 않았다. 
SimulatedAnnealing에서 이웃해를 선택 할 때, 랜덤으로 선택하는 것이 아니라 좋은 해를 선택하도록 한다면 최적해를 찾을 가능성이 더 높아질 수 있을 것이다. 
이웃해를 선택하는 방식에는 삽입, 교환, 반전 등이 있는데 숫자를 비트로 표현하면 이 방식들을 적용할 수 있다. 
이 외에도 kt값을 다르게 설정하거나 초기 온도, 냉각률 등을 바꾸어서 반복 횟수를 늘린다면 성능은 안 좋아질 수 있지만 최적해를 찾을 가능성이 더 높아질 수 있다. 
이처럼 모의 담금질 기법의 결과와 성능은 어떻게 설계하는지에 따라 달라질 수 있다.

[참고자료](https://m.blog.naver.com/allalone815/222066838078)
