Проект: Приложение для работы с сущностями Person и House.

Запуск приложения:
1. скачать проект;
2. в командной строке перейти в папку, где находится файл docker-compose.yml и выполнить команду docker-compose up -d
3. запустить приложение.

Автоматически при запуске контейнера с postgres произойдет создание базы данных, а при запуске приложения - произойдет создание таблиц  (используется liquibase) и заполнение их первичными данными.

В приложении имеются следующие возможности работы с сущностью House:
1. создание нового House с помощью json (**POST** "/api/v1/houses");
   Пример House в формате json:
```
  {
        "area": 57.7,
        "country": "RB",
        "city": "Brest",
        "street": "Solnechnaja",
        "number": 11
    }
```
2. получение House по uuid (**GET** "/api/v1/houses/{uuid}")
3. обновление House по uuid (**PUT** "/api/v1/houses/{uuid}")
4. удаление House по uuid (**DELETE** "/api/v1/houses/{uuid}")
5. получение Houses с пагинацией (**GET** "/api/v1/houses"), с параментрами page - страница, size - количество House на странице (если не задано, по умолчению 15), orderBy - по какому полю сортировать (по умолчанию "city"), direction - как сотрировать (по умолчению "ASC")
6. поиск House по стране (country), городу (city) или улице (street) (**GET** "/api/v1/houses/findBy"), с параметром name - название страны, города или улицы

В приложении имеются следующие возможности работы с сущностью Person:
1. создание нового Person с помощью json (**POST** "/api/v1/persons");
   Пример Person в формате json:
```
    {
        "name": "Ivan",
        "surname": "Ivanov",
        "sex": "MALE",
        "passportSeries": "KH",
        "passportNumber": "3234567",
        "liveHouseId": 2
    }
```
2. получение Person по uuid (**GET** "/api/v1/persons/{uuid}")
3. обновление Person по uuid (**PUT** "/api/v1/persons/{uuid}")
4. удаление Person по uuid (**DELETE** "/api/v1/persons/{uuid}")
5. получение Person с пагинацией (**GET** "/api/v1/persons"), с параментрами page - страница, pagesize - количество Person на странице (если не задано, по умолчению 15),  orderBy - по какому полю сортировать (по умолчанию "name"), direction - как сотрировать (по умолчению "ASC")

В приложении имеются следующие возможности работы с жильцами:
1. смена дома, где проживает человек (**POST** "/api/v1/residents")
```
{
    "uuidHouse": "3edfc1e2-3129-479b-9cc3-d4d3ba65df8a",
    "uuidPerson": "7780d320-e8d1-431f-a51e-abb8e3391551"
}
```
2. получить всех нынешних жильцов дома (**GET** "/api/v1/residents/{uuidHouse}")
3. получить дом, где проживает человек (**GET** "/api/v1/residents/houses/{uuidPerson}")
4. получить все дома, где когда либо проживал человек (**GET** "/api/v1/residents/houses/ever-lived/{uuidPerson}")
5. получить всех жильцов когда либо проживающих в доме (**GET** "/api/v1/residents/ever-lived/{uuidHouse}")


В приложении имеются следующие возможности работы с владельцами дома:
1. добавить человека в качестве владельца дома (**POST** "/api/v1/owners")
```
{
    "uuidHouse": "3edfc1e2-3129-479b-9cc3-d4d3ba65df8a",
    "uuidPerson": "7780d320-e8d1-431f-a51e-abb8e3391551"
}
```
2. удалить человека с владения домом (**DELETE** "/api/v1/owners")
```
{
    "uuidHouse": "3edfc1e2-3129-479b-9cc3-d4d3ba65df8a",
    "uuidPerson": "7780d320-e8d1-431f-a51e-abb8e3391551"
}
```
3. получить всех владельцев дома (**GET** "/api/v1/owners/{uuidHouse}")
4. получить все дома, которыми теперь владеет человек (**GET** "/api/v1/owners/houses/{uuidPerson}")
5. получить все дома, которыми когда либо владел человек (**GET** "/api/v1/owners/houses/ever-owned/{uuidPerson}")
6. получить всех владельцев когда либо владевшими домом (**GET** "/api/v1/owners/ever-owned/{uuidHouse}")