FROM python

RUN mkdir /bot
COPY . /bot
WORKDIR /bot
RUN python -m pip install --upgrade pip
RUN python -m pip install -r requirements.txt

ENTRYPOINT [ "python", "discraft_discord_bot.py" ]

