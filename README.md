# Discraft - плагин для майнкрафта

Проект при помощи плагина внедряет на сервер Minecraft аутентификацию через Discord-аккаунт.

Когда пользователь впервые зайдет на сервер в Minecraft, на котором стоит плагин, ему предложат привязать свой аккаунт на этом сервере к своему аккаунту в Discord.

При последующих заходах на этот сервер пользователю будет предлагаться авторизоваться с помощью кода, который ему в Discord пришлет бот.

# Запуск сервера
1. Для запуска сервера нужна [Java 8](https://www.azul.com/downloads/#download-openjdk)
 
2. Создаем папку для сервера **\server** с файлом [BuildTools.jar](https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar)

3. Собираем **Spigot** (должен создаться **spigot.jar**):
```
java -jar BuildTools.jar --rev 1.18.2
```
 
4. Создаем файл **start.bat** со следующим содержимым:
```
@echo off
java -Xms4G -Xmx4G -XX:+UseG1GC -jar spigot.jar nogui
pause
```

5. Запускаем сервер двойным кликом по **start.bat**:

* После попытки запуска сервера должен появится файл **eula.txt**. Если возникает ошибка связанная с **EULA** нужно поменять в **eula.txt**: **eula=false** на **eula=true** и перезапустить сервер.

* После попытки запуска сервера должен появится файл **server.properties**. Чтобы настроить ip сервера для локального запуска, нужно поставить **server-ip=127.0.0.1** и перезапустить сервер.

* После успешной попытки запуска сервера должна появится папка **./plugins**. Чтобы добавить данный плагин, нужно перенести в эту папку **discraft-1.0.jar** и **discraft_config.json** и перезапустить сервер.

6. После успешного запуска сервера со всеми настройками (**Done (33.126s)! For help, type "help"**) - можно заходить на сервер по адресу **server-ip=127.0.0.1** (из шага **5.2**)

# Запуск Discord-бота

**full_path_to_plugins** - полный путь до папки **...\server\plugins**, содержащей:
* папку **discraft** с файлами: **linked.json**, **login_codes.json** и **reg_codes.json**
* файлы **discraft_config.json** и **admins.json**

1. Заменить в файле **docker-compose.yaml** параметр **full_path_to_plugins**

2. Запуск
 ```
 sudo docker-compose up --build
 ```

3. Очистка после запуска
 ```
 sudo docker-compose down -v --rmi all --remove-orphans
 sudo docker rm -f $(sudo docker ps -a -q)
 ```

4. Если при запуске не работает pip
 ```
 sudo systemctl daemon-reload
 sudo systemctl restart docker
 ```
