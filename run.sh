
curl -H "Content-Type: application/json" -X POST http://localhost:8080/users/add/ \
-d '{"id":"1001","firstname": "Abdullah","lastname":"Kiwan","email":"abdullah@gmx.de"}'

printf "\nUSER ADDED SUCCESSFULLY \n"

curl -H "Content-Type: application/json" -X POST http://localhost:8080/users/add/ \
-d '{"id":"1002","firstname": "Michael","lastname":"Smith","email":"michael@gmx.de"}'

printf "\nUSER ADDED SUCCESSFULLY \n"

curl -H "Content-Type: application/json" -X POST http://localhost:8080/users/add/ \
-d '{"id":"1003","firstname": "Tim","lastname":"George","email":"tim@gmx.de"}'

printf "\nUSER ADDED SUCCESSFULLY \n"

sleep 2

curl -H "Content-Type: application/json" -X DELETE http://localhost:8080/users/delete/1003/
printf "USER DELETED SUCCESSFULLY \n"

sleep 2

curl -H "Content-Type: application/json" -X PUT http://localhost:8080/users/update/1001/ \
-d '{"id":"1001","firstname": "Abdullah","lastname":"Kiwan","email":"abdullah@gmail.de"}'
printf "\nUSER UPDATED SUCCESSFULLY \n"

sleep 2

printf "VIEW ALL USERS:\n"
curl -H "Content-Type: application/json" -X GET http://localhost:8080/users/view/all/
printf "\n"
