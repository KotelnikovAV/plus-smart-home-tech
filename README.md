# Приложение SmartHome.
## 1. Описание проекта.
### 1.1 Идея приложения.
SmartHome — это приложение, объединяющее интернет-магазин оборудования для умного дома и платформу
анализа телеметрии датчиков. Нацеленное на удобство и функциональность, приложение позволяет пользователям не
только приобретать необходимые устройства, но и эффективно управлять их работой, обеспечивая безопасность и
комфорт.  
Основная цель данного проекта - изучение и применение на практике Spring Cloud и Kafka.
### 1.2 Основная функциональность приложения.
Приложение состоит из двух частей: небольшой интернет-магазин оборудования для умного дома, платформа для анализа 
телеметрии датчиков умного дома.

Основная функциональность интернет-магазина:
- модуль shopping-store — представляет собой витрину товаров, из которых пользователи выбирают товары для заказа;
- модуль shopping-cart — отвечает за работу с корзиной пользователей. Пользователи будут добавлять товары в корзину, 
чтобы сделать заказ;
- модуль order — работает с заказами пользователей, оформляет их;
- модуль warehouse — отвечает за склад;
- модуль payment — в этом сервисе сосредоточена логика, связанная с оплатой заказов;
- модуль delivery — ответственен за доставку готовых заказов пользователям.

Основная функциональность платформы для анализа телеметрии:
- обрабатывает данные со всех проданных датчиков;
- трансформирует их в нужный формат;
- хранит описание сценариев: какую команду запустить и при каких показателях от конкретных датчиков;
- определяет необходимость запуска этих сценариев.

### 1.3 Стек используемых технологий.
Основные технологии и инструменты, используемые в данном проекте:
- Java (Amazon Corretto 21.0.6);
- Maven;
- Spring Boot;
- Spring Cloud;
- PostgreSQL;
- Hibernate ORM;
- Docker;
- Kafka;
- REST;
- gRPC.

## 2. Инструкция по развертыванию проекта.
Для запуска приложения необходимо:
- склонировать проект: https://github.com/KotelnikovAV/plus-smart-home-tech.git;
- открыть проект с помощью IntelliJ IDEA и выполнить команду mvn package;
- запустить исполнение файла compose.yml.

После успешного исполнения файла docker-compose.yml приложение будет доступно на порту 8080.

## 3. Техническая документация проекта.
Спецификация API в формате openapi:
