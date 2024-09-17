from flask import Flask
from flask import send_file
import os

app = Flask(__name__)


cache = {'last': -1}


# Print the screen with a count from 0 - 9 to imitate a dynamic screen
@app.route('/screenshot')
def screenshot():

    if cache['last'] < 9:
        cache['last'] += 1

    elif cache['last'] == 9:
        cache['last'] = 0

    img = 'screen' + str(cache['last']) + '.png'
    filename = os.path.join('static', img)
    return send_file(filename, mimetype='image/png')


@app.route('/screenshot-static')
def screenshot_static():
    filename = os.path.join('static', 'screen.png')
    return send_file(filename, mimetype='image/png')


@app.route('/')
def home():
    return "Please use /screeshot route to get the image"


if __name__ == '__main__':
    app.run(host="localhost")
