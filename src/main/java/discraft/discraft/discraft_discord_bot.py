from json.tool import main
import discord
import random
import string
import json
import datetime
from os.path import exists
from discord.ext import commands

config = {
    "token": "",
    "prefix": "/",
}

client = commands.Bot(command_prefix=config["prefix"])
comms = ["/info", "/login"]


@client.event
async def on_ready():
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Bot is ready")
    print("_____________________________________________________________________")

@client.event
async def on_message(message):
    if message.author != client.user and message.content not in comms:
        user = str(message.author)
        code = str(message.content)
        print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Registration started: " + user)
        with open('discraft/linked.json') as f:
            linked = json.load(f)
        with open('discraft/reg_codes.json') as f:
            reg_codes = json.load(f)
        with open('discraft/login_codes.json') as f:
            login_codes = json.load(f)
        reversed_regs = {v: k for k, v in reg_codes.items()}
        if code in reg_codes.values():
            linked[reversed_regs[code]] = user
            login_codes[reversed_regs[code]] = "None"
            reg_codes.pop(reversed_regs[code])
            with open('discraft/linked.json', 'w') as f:
                json.dump(linked, f)
            with open('discraft/reg_codes.json', 'w') as f:
                json.dump(reg_codes, f)
            with open('discraft/login_codes.json', 'w') as f:
                json.dump(login_codes, f)
            await message.reply("Registration was successful, now you can login as ***" + str(reversed_regs[code]) + "***.")
            print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Registration was successful: " + user)
        else:
            await message.reply("Your code is incorrect, please try again or write `/info`.")
            print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Registration failed: " + user)
    if message.content == "/info":
        await info(message)
    if message.content == "/login":
        await login(message)

async def info(message):
    user = str(message.author)
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Info request: " + user)
    await message.reply("Hello! I'm bot for minecraft plugin **'Discraft'**. For authorization you need to go to the server with this plugin. If you need to log in, write `/login`.")

async def login(message):
    user = str(message.author)
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Login started: " + user)
    with open('discraft/linked.json') as f:
        linked = json.load(f)
    with open('discraft/login_codes.json') as f:
        login_codes = json.load(f)
    reversed_linked = {v: k for k, v in linked.items()}
    if user not in linked.values() or reversed_linked[user] not in list(login_codes):
        await message.reply("Please reg first or write `/info`.")
        print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | User no reg error: " + user)
        return
    code = ''.join(random.choices(string.digits, k=6))
    login_codes[reversed_linked[user]] = code
    with open('discraft/login_codes.json', 'w') as f:
        json.dump(login_codes, f)
    await message.reply(code)
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Code sended: " + user)

if not exists('discraft_config.json'):
    print('CONFIG NOT FOUND!')
    exit(1)
with open('discraft_config.json') as f:
    conf = json.load(f)
    config['token'] = conf['bot_token']
client.run(config["token"])