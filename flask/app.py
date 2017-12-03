#!/home/augusto/Developer/JogoAfrica/flask/bin/python3
from flask import Flask, jsonify, request, abort
from random import randrange
from json import dumps, loads

app = Flask(__name__)


class Session:
	def __init__(self):
		self.connected = 0
		self.words = []
		self.nome_equipes = []
		self.equipes = {}
		self.timestamp = 0

a = Session()
a.nome_equipes = ["Equipe 1", "Equipe 2"]
sessions = {1: a}

@app.route('/create_session', methods=['GET'])
def new_session():
	session = randrange(99999)
	while session in sessions.keys():
		session = randrange(99999)
	sessions[session] = Session()
	print(sessions.keys())
	return str(session)

@app.route('/send_teams/<int:session>', methods=['POST'])
def send_teams(session):
	if not request.json or session not in sessions:
		abort(400)

	sessions[session].nome_equipes.extend(request.get_json())
	print(sessions[session].nome_equipes)
	return "", 201

@app.route('/get_teams/<int:session>', methods=['GET'])
def get_teams(session):
	if session not in sessions:
		abort(400)
	return dumps(sessions[session].nome_equipes)
	
@app.route('/send_team_members/<int:session>', methods=['POST'])
def send_team_members(session):
	if not request.json or session not in sessions:
		abort(400)
	for obj in request.get_json():
		if obj.get('equipe') not in sessions[session].equipes:
			sessions[session].equipes[obj.get('equipe')] = [obj.get('nome')]
		else:
			sessions[session].equipes[obj.get('equipe')].append(obj.get('nome'))
		sessions[session].words.extend(obj.get('palavras'))
	print(sessions[session].words)
	print(sessions[session].equipes)
	return "", 201


@app.route('/send_words/<int:session>', methods=['POST'])
def send_word(session):
	if not request.json or session not in sessions:
		abort(400)

	sessions[session].words.extend(request.get_json())
	print(sessions[session].words)
	return "", 201

@app.route('/get_words/<int:session>', methods=['GET'])
def get_words(session):
	if session not in sessions:
		abort(400)

	print(dumps(sessions[session].words))
	return dumps(sessions[session].words), 200

@app.route('/is_words_complete/<int:session>', methods=['GET'])
def is_words_complete(session):
	if session not in sessions:
		abort(400)

	num_jogadores = sum([len(j) for _, j in sessions[session].nome_equipes])
	return str(len(sessions[session].words) >= num_jogadores), 200	

@app.route('/start_timer/<int:session>', methods=['POST'])
def start_timer(session):
	if not request.json or session not in sessions:
		abort(400)

	if sessions[session].timestamp == 0:
		sessions[session].timestamp = int(request.get_json()['timestamp'])
		print(sessions[session].timestamp)
		return "", 201
	else:
		return str(sessions[session].timestamp), 200

@app.route('/stop_timer/<int:session>', methods=['POST'])
def stop_timer(session):
	if not request.json or session not in sessions:
		abort(400)

	sessions[session].timestamp = 0
	sessions[session].words = request.get_json()
	print(sessions[session].words)
	return "", 201


if __name__ == '__main__':
    app.run(debug=True)