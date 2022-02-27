
# Credit Application System

Bu proje, mevcut müşteriler veya yeni müşteriler için kredi oluşturma ve kredi sonucunun görüntülenmesini içerir.

## Özet
Bu projede yetkili kullanıcı tarafından kullanıcı oluşturulabilir ve güncellenebilir. Mevcut kullanıcılar, görüntülenebilir ve silinebilir.

Kullanıcılar tarafından ise kredi başvurusu oluşturulabilir ve başvuru sonucu kullanıcı kimlik numarası ile görüntülenebilir.
 
Kredi skoru mevcut müşterilerin kredi skorlarına göre başvuruları kabul edilir ve kredi limiti belirlenir. Kredi skoru belli bir limitin altında olan kullanıcıların ise başvuruları direkt olarak reddedilir. 
Kredi skoru database'de mevcut olmayan kullanıcıların başvuruları da direkt olarak reddedilir. 

### Gereksinimler
**Login**
username: ufukbayraktar
password: 123456

Swagger ve Postman kullanarak UserAPI üzerinde işlem gerçekleştirmek için gerekli kullanıcı bilgileri yukarıdaki gibidir.

**TC Kimlik Numarası Üreticisi**
User Entity ve Request objelerinde yer alan identityNumber'ın TcKimlikNo validasyonu mevcuttur. 
Bu yüzden istekleri gerçekleştirirken gerçek kimlik numaraları olmak zorundadır. Bunun için, 
https://www.simlict.com/ bu adresten faydalanabilirsiniz.

**Postman**
Projenin örnek requestlerini aşağıdaki ilgili link üizerinden indirerek Postman üzerinden import edip kullanabilirsiniz. 

https://www.getpostman.com/collections/0996486454e965355577

Yapılan başvurudaki kullanıcının skor tablosunda bir kredi skoru kaydı yoksa servis direkt olarak reddeder.
Başvuran kullanıcının skor değerini tanımlamak için data.sql deki gibi sorgular kullanılabilir.

**DB**
DB kurulumu için proje içerisindeki schema.sql çalıştırılması gerekmektedir.

## Teknolojiler

#### <u>Backend</u>

 - Java 11 
 - Spring Boot 
 - Hibernate  
 - PostgreSql   
 - Docker   
 - Junit

## Database ER Diagram
![E-R Diagram](docs/ER%20Diagram.png "ER Diagram")

### Backend Uygulaması Başlatma
```
git clone https://github.com/ufukbayraktar/paycore-credit-applicaton-project.git
$ mvn clean install
$ mvn spring-boot:run
```
Uygulama varsayılan olarak 8080 numaralı port üzerinden başlayacaktır.

## Swagger

http://localhost:8080/swagger-ui.html

UserAPI kullanılabilmesi için gereksinimlerde verilmiş olan kullanıcı bilgileri kullanılması gerekmektedir.

![Swagger](docs/Swagger%20Controllers.png "Swagger Controllers.")

2 adet API mevcuttur. Bunlardan **UserAPI** için authorization işlemi gerçekleştirilmesi gerekmektedir.
**ApplicationAPI** için bir authorization işlemine gerek yoktur.


## UserAPI

Authorization gerçekleşmediği takdirde istekler aşağıdaki gibi sonuç dönmektedir.
```
{ 
"errorMessage": "Authentication Exception", 
"errorCode": "2003",
"errorHttpStatus": "401 UNAUTHORIZED"
 }
```

**api/user/create** ve **api/user/update** işlemleri için aşağıdaki gibi json body gerekmektedir.
```
{
"identityNumber": 64220993016,
"name": "Testname",
"surname": "testsurname",
"phoneNumber": "5554443321",
"salary": 99999
}
```
**api/user/create** ve **api/user/update** için json body içindeki keylerden herhangi biri yanlış girildiğinde o değer ile ilgili bilgi aşağıdaki gibi  sonuç mesajında yer almaktadır. 
```
{
"code": "2001",
"message": "[IdentityNumber is invalid., PhoneNumber must contain numbers only]",
"status": "BAD_REQUEST"
}
```


**api/user/create**  için var olan bir kullanıcı identityNumber girildiğinde aşağıdaki gibi sonuç alınmaktadır.
```
{
"code": "2004",
"message": "User Already Exists Exception",
"status": "NOT_ACCEPTABLE"
}

```
**api/user/delete/{identityNumber}** ve **api/user/getUser/{identityNumber}** işlemleri için ise url kısmına identityNumber girilmesi gerekmektedir.

**api/user/delete** , **api/user/getUser**  ve **api/user/update**  işlemleri için kullanıcı bulunamadığında aşağıdaki gibi sonuç alınmaktadır.
```
{
"code": "2002",
"message": "User Not Found Exception",
"status": "NOT_FOUND"
}
```


## ApplicationAPI

Kullanıcı başvuru gerçekleştirirken kimi kullanıcıların skore tablosunda skorları tutulmaktadır. Bu kullanıcılar başvuru yaptıklarında başvuru durumları **REJECTED** ve **CONFIRMED** olarak iki sonuç dönebilmektedir.
Skoru tabloda olup 500'ün altında olan ve skoru hiç bulunmayan kullanıcılar reddedilirken, skor tablosunda kredi skoru bulunup bu skor 500'ün üzerindeyse verilen kriterlere göre kredi limit belirlenmektedir.


**api/application/create**  işlemi için aşağıdaki gibi json body gerekmektedir.

Aşağıdaki örnek kullanıcımız 750 kredi skoruna sahip bir kullanıcıdır.
```
{
"identityNumber": 64220993016,
"name": "Testname",
"surname": "testsurname",
"phoneNumber": "5554443321",
"salary": 42000
}
```
750 kredi skoru ve yukarıdaki bilgilere sahip kullanıcı aşağıdaki gibi bir sonuç almaktadır.
```
{
"status": "0",
"message": "success",
"data": {
"status": "CONFIRMED",
"creditLimit": 20000 
		}
}
```
Eğer kredi skoru olmayan veya 500'ün altında kredi skoruna sahip bir kullanıcı başvuru yaparsa aşağıdaki gibi sonuç alınmaktadır.
```
{
"status": "0",
"message": "success",
"data": {
"status": "REJECTED",
"creditLimit": 0
		}
}
```


**api/application/get-status/{identityNumber}** işlemi için  ise url kısmına identityNumber girilmesi gerekmektedir.  

Başvurusu reddedilmiş bir kullanıcı için başvuru durumunu sorguladığımızda ise aşağıdaki gibi sonuç alınmaktadır.
```
{
    "status": "0",
    "message": "success",
    "data": [
        {
            "status": "REJECTED",
            "creditLimit": 0.00
        }
		    ]
}
```

