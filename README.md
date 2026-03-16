# Website-WebSocket
Этот репозиторий для временного изучения WebSocket

### Как использовать?
1. Запускаем проект
2. На сайте используем F12 и пишем следующию команду:
```
const ws = new WebSocket("ws://localhost:8080/echo");
ws.onopen = () => {
console.log("Подключено!");
ws.send("Тестовое сообщение без авторизации!");
};

ws.onmessage = (e) => {
console.log("От сервера: " + e.data);
};

ws.onerror = (err) => console.error("Ошибка WS: ", err);
ws.onclose = () => console.log("Соединение закрыто!");
```
3. После можем использовать команду: ```ws.send(text)```
4. Чтобы закрыть соединение используем команду:```ws.close()```
