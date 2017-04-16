def parse(beach, name):
    msg = name + " is enjoying at " + beach.getName() + " on " + beach.getCity()
    beach.setMsg(msg)
    return msg
