#!/usr/bin/env python3

import urllib.error 
import urllib.request as req
import datetime as dt
import json
import argparse as arg

from notify import notify_telegram

NORMAL_STATUS = "UP"

health_url = "http://127.0.0.1:8283/health"

now = dt.datetime.now().isoformat()
date_str = now + "\n"
message = [date_str]

def is_status_ok(status: str) -> bool:
    return status == NORMAL_STATUS

def check_health(content) -> bool:
    res = True
    content = json.load(resource)

    shopbot_status = content["status"]
    database_status = content["details"]["jdbc"]["status"]

    if is_status_ok(shopbot_status):
        res = res and True
    else:
        res = res and False
        message.append("ALERT: shopbot is not running\n")

    if database_status == NORMAL_STATUS:
        res = res and True
    else:
        res = res and False
        message.append("ALERT: database is not runnig\n")
    
    return res 

def check_log(log_path) -> bool:
    res = True
    with open(log_path) as file:
        for line in file:
            if "ERROR" in line:
                res = res and False
                message.append("ALERT: find error in log file: {}\n".format(log_path))
    return res

def prepare_err(exc: urllib.error.HTTPError) -> str:
    return "ERROR: http error during shopbot health check.\nresponse code: {0.code}\nreason: {0.reason}".format(exc)

if __name__ == "__main__":

    parser = arg.ArgumentParser(description="Send message to tlegram")
    parser.add_argument("bot_token", type=str)
    parser.add_argument("chat_id", type=int)
    parser.add_argument("log_file_path", type=str)
    args = parser.parse_args()
    bot_token = args.bot_token
    chat_id = args.chat_id
    log_file = args.log_file_path

    try:
        resource = req.urlopen(health_url)
        if not (check_health(resource) and check_log(log_file)):
            notify_telegram(bot_token, chat_id, "".join(message))
    except urllib.error.HTTPError as e:
        message.append(prepare_err(e))
        notify_telegram(bot_token, chat_id, "".join(message))
