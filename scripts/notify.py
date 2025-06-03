#!/usr/bin/env python
import argparse as arg
from urllib.request import urlopen, Request
from json import dumps

def notify_telegram(bot_token: str, chat_id: int, message: str) -> None:
    body = {"chat_id": chat_id, "text": message}
    prepeared_body = dumps(body).encode("utf-8")
    telegramBotUrl = "https://api.telegram.org/bot{}/sendMessage".format(bot_token)
    request = Request(telegramBotUrl)
    request.add_header("Content-Type", "application/json; charset=utf-8")
    request.add_header("Content-Length", len(prepeared_body))
    urlopen(request, prepeared_body)

if __name__ == "__main__":
    parser = arg.ArgumentParser(description="Send message to tlegram")
    parser.add_argument("bot_token", type=str)
    parser.add_argument("chat_id", type=int)
    parser.add_argument("message", type=str)

    args = parser.parse_args()
    notify_telegram(args.bot_token, args.chat_id, args.message)
