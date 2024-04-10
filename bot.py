# pip install -U g4f
# pip install pyTelegramBotAPI
# pip install python-telegram-bot

import telebot
import re
from g4f.client import Client

client = Client()

bot = telebot.TeleBot("6284136030:AAG3RzHcKR8ItpssmrovSEufFYW3k73Kmw8")

@bot.message_handler(func=lambda message: True)
def echo_message(message):
  if re.match("@Tryamkins_bot", message.text):
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "user", "content": message.text }],
    )
    bot.send_message(message.chat.id, response.choices[0].message.content)

bot.polling()