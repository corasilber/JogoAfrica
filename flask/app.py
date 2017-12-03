#!/home/augusto/Developer/JogoAfrica/flask/bin/python3
from flask import Flask, jsonify
from random import randrange

app = Flask(__name__)

sessions = {}
tasks = [
    {
        'id': 1,
        'title': u'Buy groceries',
        'description': u'Milk, Cheese, Pizza, Fruit, Tylenol', 
        'done': False
    },
    {
        'id': 2,
        'title': u'Learn Python',
        'description': u'Need to find a good Python tutorial on the web', 
        'done': False
    }
]

@app.route('/create_session', methods=['GET'])
def new_session():
	session = randrange(99999)
	while session in sessions.keys():
		session = randrange(99999)
	sessions[session] = []
	print(sessions.keys())
	return str(session)

@app.route('/todo/api/v1.0/tasks', methods=['GET'])
def get_tasks():
    return jsonify({'tasks': tasks})

if __name__ == '__main__':
    app.run(debug=True)