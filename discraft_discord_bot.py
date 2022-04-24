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
comms = ["/info", "/login", "/unlink", "/commands", "/stop"]


@client.event
async def on_ready():
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Bot is ready")
    print("_____________________________________________________________________")


@client.event
async def on_message(message):
    if message.author != client.user and message.content not in comms:
        user = str(message.author)
        code = str(message.content)
        print(datetime.datetime.today().strftime(
            "%d.%m.%Y %H:%M:%S") + " | 'Not command'-message received: " + user)
        with open('discraft/linked.json') as f:
            linked = json.load(f)
        with open('discraft/reg_codes.json') as f:
            reg_codes = json.load(f)
        with open('discraft/login_codes.json') as f:
            login_codes = json.load(f)
        reversed_regs = {v: k for k, v in reg_codes.items()}
        if not await check_reg(message, user):
            await message.reply("You are already registered, write `/info` for more information.")
            print(datetime.datetime.today().strftime(
                "%d.%m.%Y %H:%M:%S") + " | User is already registered: " + user)
            return
        print(datetime.datetime.today().strftime(
            "%d.%m.%Y %H:%M:%S") + " | Registration started: " + user)
        if not code.isdecimal():
            await message.reply("Invalid code format, please try again.")
            print(datetime.datetime.today().strftime(
                "%d.%m.%Y %H:%M:%S") + " | Invalid code: " + user)
            return
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
            await message.reply("Registration was successful, now you can login as ***" + str(
                reversed_regs[code]) + "***.")
            print(datetime.datetime.today().strftime(
                "%d.%m.%Y %H:%M:%S") + " | Registration was successful: " + user)
        else:
            await message.reply("Your code is incorrect, please try again or write `/info`.")
            print(datetime.datetime.today().strftime(
                "%d.%m.%Y %H:%M:%S") + " | Registration failed: " + user)
    if message.content == "/info":
        await info(message)
    if message.content == "/login":
        await login(message)
    if message.content == "/unlink":
        await unlink(message)
    if message.content == "/stop":
        await stop(message)
    if message.content == "/commands":
        await command_list(message)


async def info(message):
    user = str(message.author)
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Info request: " + user)
    await message.reply(
        "Hello! I'm bot for minecraft plugin **'Discraft'**. For authorization you need to go to the server with this plugin. If you need to log in, write `/login`.")


async def login(message):
    user = str(message.author)
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Login started: " + user)
    with open('discraft/linked.json') as f:
        linked = json.load(f)
    with open('discraft/login_codes.json') as f:
        login_codes = json.load(f)
    if await check_reg(message, user):
        await message.reply("Please reg first or write `/info`.")
        return
    reversed_linked = {v: k for k, v in linked.items()}
    code = ''.join(random.choices(string.digits, k=6))
    login_codes[reversed_linked[user]] = code
    with open('discraft/login_codes.json', 'w') as f:
        json.dump(login_codes, f)
    await message.reply(code)
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Code sent: " + user)


async def unlink(message):
    user = str(message.author)
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Unlink started: " + user)
    with open('discraft/linked.json') as f:
        linked = json.load(f)
    with open('discraft/login_codes.json') as f:
        login_codes = json.load(f)
    reversed_linked = {v: k for k, v in linked.items()}
    if await check_reg(message, user):
        await message.reply("Please reg first or write `/info`.")
        print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Unlink failed: " + user)
        return
    login_codes.pop(reversed_linked[user])
    linked.pop(reversed_linked[user])
    with open('discraft/login_codes.json', 'w') as f:
        json.dump(login_codes, f)
    with open('discraft/linked.json', 'w') as f:
        json.dump(linked, f)
    await message.reply("User was unlinked: ***" + reversed_linked[user] + "***.")
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | Unlink was successful: " + user)


async def command_list(message):
    user = str(message.author)
    await message.reply("Command list: " + str(comms[:-2]))
    print(datetime.datetime.today().strftime("%d.%m.%Y %H:%M:%S") + " | Commands request: " + user)


async def check_reg(message, user):
    with open('discraft/linked.json') as f:
        linked = json.load(f)
    if user not in linked.values():
        print(datetime.datetime.today().strftime(
            "%d.%m.%Y %H:%M:%S") + " | User no reg error: " + user)
        return True
    return False


async def stop(message):
    with open('admins.json') as f:
        admins = json.load(f)
    if str(message.author) not in admins:
        return
    await message.reply("Oyasumi.")
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | The bot was stopped by " + str(message.author))
    exit(1)


if not exists('discraft_config.json'):
    print('CONFIG NOT FOUND!')
    exit(1)
with open('discraft_config.json') as f:
    conf = json.load(f)
    config['token'] = conf['bot_token']
client.run(config["token"])
