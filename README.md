# Selenide Junit Project Example
Simple project with Selenide and JUnit

## Запуск тестов
### Из консоли
* run `mvn clean test -Dlogin=login -Dpassword=password`

где 'login' и 'password' - соответственно, логин и пароль Пользователя.

### Из вкладки Maven
Запустить задачу "Test", предварительно настроив JVM-параметры, как описано выше.

## Для получения отчёта
### Из консоли
Запуск команды 
* mvn allure:report
далее
* mvn allure:serve

### Из GUI
Запустить 
* Plugins -> allure -> allure:report
далее
* Plugins -> allure -> allure:serve

