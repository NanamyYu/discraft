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

help = discord.Embed(color=0xb84dff, title='Commands list')
help.add_field(name='/reg <code>', value='Registration, format: `/reg <code>`. In order to get a code connect to the Minecraft server.', inline=False)
help.add_field(name='/info', value='Shows this message, format: `/info`.', inline=False)
help.add_field(name='/login', value='Sends you a code for authorization, format: `/login`.', inline=False)
help.add_field(name='/unlink', value='Deletes all your data from Discraft\'s database, format: `/unlink`.', inline=False)


@client.event
async def on_ready():
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | Bot is ready")
    print("_____________________________________________________________________")

@client.command()
async def reg(ctx, arg):
    if ctx.message.author == client.user:
        return
    user = str(ctx.message.author)
    code = str(arg)
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | Registration started: " + user)
    linked = await open_linked()
    reg_codes = await open_reg()
    login_codes = await open_login()
    reversed_regs = {v: k for k, v in reg_codes.items()}
    if not await check_reg(user):
        await ctx.message.reply(
            "You are already registered, write `/info` for more information.")
        print(datetime.datetime.today().strftime(
            "%d.%m.%Y %H:%M:%S") + " | User is already registered: " + user)
        return
    if not code.isdecimal():
        await ctx.message.reply(
            "Invalid code format, please try again.")
        print(datetime.datetime.today().strftime(
            "%d.%m.%Y %H:%M:%S") + " | Invalid code: " + user)
        return
    if code in reg_codes.values():
        linked[reversed_regs[code]] = user
        print(linked[reversed_regs[code]])
        login_codes[reversed_regs[code]] = "None"
        reg_codes.pop(reversed_regs[code])
        await close_linked(linked)
        await close_reg(reg_codes)
        await close_login(login_codes)
        await ctx.message.reply(
            "Registration was successful, now you can login as ***" + str(reversed_regs[code]) + "***.")
        print(datetime.datetime.today().strftime(
            "%d.%m.%Y %H:%M:%S") + " | Registration was successful: " + user)
    else:
        await ctx.message.reply(
            "Your code is incorrect, please try again or write `/info`.")
        print(datetime.datetime.today().strftime(
            "%d.%m.%Y %H:%M:%S") + " | Registration failed: " + user)


@client.command()
async def info(ctx):
    user = str(ctx.message.author)
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | Info request: " + user)
    await ctx.message.reply(embed=help)  
    
@client.command()
async def login(ctx):
    user = str(ctx.message.author)
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | Login started: " + user)
    linked = await open_linked()
    login_codes = await open_login()
    if await check_reg(user):
        await ctx.message.reply(
            "Please reg first or write `/info`.")
        return
    reversed_linked = {v: k for k, v in linked.items()}
    code = ''.join(random.choices(string.digits, k=6))
    login_codes[reversed_linked[user]] = code
    await close_login(login_codes)
    await ctx.message.reply(code)
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | Code sent: " + user)

@client.command()
async def unlink(ctx):
    user = str(ctx.message.author)
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | Unlink started: " + user)
    linked = await open_linked()
    login_codes = await open_login()
    reversed_linked = {v: k for k, v in linked.items()}
    if await check_reg(user):
        await ctx.message.reply("Please reg first or write `/info`.")
        print(datetime.datetime.today().strftime(
            "%d.%m.%Y %H:%M:%S") + " | Unlink failed: " + user)
        return
    login_codes.pop(reversed_linked[user])
    linked.pop(reversed_linked[user])
    await close_login(login_codes)
    await close_linked(linked)
    await ctx.message.reply("User was unlinked: ***" + reversed_linked[user] + "***.")
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | Unlink was successful: " + user)
    
    

async def check_reg(user):
    linked = await open_linked()
    if user not in linked.values():
        print(datetime.datetime.today().strftime(
            "%d.%m.%Y %H:%M:%S") + " | User no reg error: " + user)
        return True
    return False

async def open_linked():
    with open('discraft/linked.json') as f:
        linked = json.load(f)
    return linked

async def open_reg():
    with open('discraft/reg_codes.json') as f:
        reg_codes = json.load(f)
    return reg_codes

async def open_login():
    with open('discraft/login_codes.json') as f:
        login_codes = json.load(f)
    return login_codes

async def close_login(login_codes):
    with open('discraft/login_codes.json', 'w') as f:
        json.dump(login_codes, f)
    return
    
async def close_linked(linked):
    with open('discraft/linked.json', 'w') as f:
        json.dump(linked, f)
    return

async def close_reg(reg_codes):
    with open('discraft/reg_codes.json', 'w') as f:
        json.dump(reg_codes, f)
    return

@client.command()
async def stop(ctx):
    with open('admins.json') as f:
        admins = json.load(f)
    if str(ctx.message.author) not in admins:
        return
    await ctx.message.reply("Oyasumi.")
    print(datetime.datetime.today().strftime(
        "%d.%m.%Y %H:%M:%S") + " | The bot was stopped by " + str(ctx.author))
    exit(1)

    
if not exists('discraft_config.json'):
    print('CONFIG NOT FOUND!')
    exit(1)
with open('discraft_config.json') as f:
    conf = json.load(f)
    config['token'] = conf['bot_token']
client.run(config["token"])

