# Restaurant Booking
Web application for booking restaurant.


Zbudowac front:   
  ->wejsc do katalogu booking-app-front   
  ->npm install  
Wystartowac front:  
  ->npm start  
  
wyswietlic na: localhost:4200


curl:

register:
curl -H "Content-Type: application/json" -X POST -d '{
    "username": "admin",
    "password": "admin"
}' http://localhost:8080/register



login
curl -H "Content-Type: application/json" -X POST -d '{
    "username": "admin",
    "password": "admin"
}' http://localhost:8080/api/auth



later:
curl -H "Authorization: Bearer xxx.yyy.zzz" http://localhost:8080/api/hello-world  
curl -H "Authorization: Bearer xxx.yyy.zzz" http://localhost:8080/api/restaurants/getAll
itd...