# Инструкция по запуску
## Предварительные требования
- Убедитесь, что у вас установлены Docker и Docker Compose.
- Убедитесь, что у вас установлен Maven.
## Шаги по запуску
- Перейдите в корневую директорию проекта и соберите его с помощью Maven <br>
  ``` mvn clean package ```
- Создайте Docker образ <br>
  ``` docker build . -t your-app-name ```
- Запуск Docker Compose <br>
  ``` docker compose up --build ```
- Ознакомтесь с документацией по адресу: <br>
  `http://localhost:8080/app/swagger-ui.html`