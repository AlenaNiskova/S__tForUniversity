SoftForUniversity
# Требования к приложению:
Splash Screen;

Меню:

*	Аутентификация на сервере github и выход (logout);
*	Окно списка репозиториев пользователя;
*	Карта с отображением маршрута из адреса университета до дома, используя какой-либо вид транспорта (условная точка);
* Отображение списка контактов из телефонной книги;
*	Отображение основной информации об устройстве (ip, android_version и т.д.);
*	Отображение изменяемых значений работы одного из датчиков и получения фотографии с последующим сохранением в память.
# Задачи приложения и их описание:
Splash Screen – загрузочный экран, отображается при первичном открытии приложения. Цель: инициализация сервисов и служб. После него сразу же открывается активити с меню, в котором уже выбран пункт "Login/logout".

# Авторизация и репозитории:

На окне аутентификации присутствует поле login и password, после верного введения своих учетных данных на GitHub и нажатии кнопки Sign In происходит проверка наличия у пользователя второй аутентификации и при наличии таковой предлагается ввести код, после чего происходит вход, если же её нет то вход происходит без запроса кода.

Когда вход в GitHub ещё не выполнен, header меню, а также фрагменты авторизации и репозиториев выглядят так:

![](https://pp.userapi.com/c845020/v845020430/177454/FTJ_I__aHUk.jpg)
![](https://pp.userapi.com/c845020/v845020430/17747e/UP6qL8xqPFQ.jpg)
![](https://pp.userapi.com/c845020/v845020430/177477/o5mFuxr7Z7M.jpg)

Для входа в гитхаб также может потребоваться код в случае, если у человека включена двухфакторная аутентификация:

![](https://pp.userapi.com/c845020/v845020430/17749d/9bSpdCMv098.jpg)

После того, как вход в GitHub будет выполнен, header меню и фрагменты будут выглядеть следующим образом: header меню будет содержать в себе логин авторизованного пользователя, фрагмент авторизации будет содержать всего одну кнопку Sign out, а фрагмент репозиториев будет содержать репозитории (если они есть).

![](https://pp.userapi.com/c845020/v845020430/1774e9/WvLfLOHi4-k.jpg)
![](https://pp.userapi.com/c845020/v845020430/1774a4/6C5YTnyYHnw.jpg)
![](https://pp.userapi.com/c845020/v845020430/17744d/eIqGV02Xths.jpg)

# Информация об устройстве, карта, контакты:

При выборе пункта меню с информацией открывается основная информация об устройстве, на котором запущено приложение.

При выборе карт будет открыта карта Google с отрисованным маршрутом до кампуса МГУПИ. Режим передвижения - на машине, кнопки приближения/отдаления присутствуют.

"Contacts" показывает все имеющиеся на устройстве контакты с их номерами (замазаны).

![](https://pp.userapi.com/c845020/v845020430/177446/XUjFMgXyW_E.jpg)
![](https://pp.userapi.com/c845020/v845020430/177469/xz0lNb24uFw.jpg)
![](https://pp.userapi.com/c846416/v846416703/17ba38/oUjoxtXxTc0.jpg)

# Датчики и камера:

На фрагменте можно наблюдать показания гироскопа. Ниже представлены скрины фрагмента в трёх состояниях: до того, как фото сделано; после того, как фото сделано; после того, как фото сохранено.

![](https://pp.userapi.com/c845020/v845020430/177496/tCBanjJFq6o.jpg)
![](https://pp.userapi.com/c845020/v845020430/17745b/mAo94796IMc.jpg)
![](https://pp.userapi.com/c845020/v845020430/177462/9zjtbsUQn9U.jpg)

# Карта переходов

Основные обозначения:

![](https://pp.userapi.com/c845017/v845017150/17f4e1/ZojF4-Rf04k.jpg)

Карта:

![](https://pp.userapi.com/c845017/v845017150/17f4e9/VJm8sPqLjCo.jpg)

# Краткое описание использованных шаблонов проектирования

В данном проекте применяется шаблон проектирования Model-View-Presenter, с общим принципом реализации, показанным на блок-схеме ниже.

![](https://pp.userapi.com/c845017/v845017703/18029b/PupFH4xA5T0.jpg)

В роли view, в общем виде, выступает фрагмент программы - GitHubFragment.

В классе фрагмента существует экземпляр класса для взаимодействия с презентером:
```java
private GitHubPresenter gitHubPresenter;
```

При создании фрагмента происходит инициализация презентера:
```java
if (gitHubPresenter == null) {
    gitHubPresenter = new GitHubPresenter(this);
}
```
таким образом модули завязываются друг на друга.

В качестве модели выступает класс GitHubModel. С его помощью через AsyncTask данные для авторизации пользователя отправляются на сервер.

Для доступа к контактам, камере и прочему производятся запросы runtimepermission.

# Описание API приложения

Основной функционал приложения доступен без обязательной авторизации на GitHub.

Авторизация по желанию пользователя даст ему возможность просмотреть список репозиториев.

API обладает следующими функциями:

* Показывать репозитории авторизовавшегося в GitHub пользователя;
* Выводить основную информацию об устройстве;
* Строить на карте Google маршрут от дома (задано в программе) до кампуса на Стромынке, 20;
* Показывать список контактов, которые есть на устройстве;
* Выводить данные акселерометра, создавать и сохранять фотографии.
