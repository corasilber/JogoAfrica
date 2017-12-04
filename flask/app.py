#!/home/augusto/Developer/JogoAfrica/flask/bin/python3
from flask import Flask, jsonify, request, abort
from random import randrange
from json import dumps, loads
from time import time
from datetime import timedelta

app = Flask(__name__)


class Session:
	def __init__(self):
		self.connected = 0
		self.words = []
		self.nome_equipes = []
		self.equipes = {}
		self.timestamp = 0
		self.num_jogadores = 0
		self.words_version = 0

a = Session()
a.nome_equipes = ["Equipe 1", "Equipe 2"]
sessions = {1: a}

@app.route('/create_session/', methods=['GET'])
def new_session():
	session = randrange(99999)
	while session in sessions.keys():
		session = randrange(99999)
	sessions[session] = Session()
	print(sessions.keys())
	return str(session)

@app.route('/send_teams', methods=['POST'])
def send_teams():
	session = int(request.args.get('session'))
	num_jogadores = int(request.args.get('num_jogadores'))
	if not request.json or session not in sessions:
		abort(400)

	sessions[session].nome_equipes.extend(request.get_json())
	sessions[session].num_jogadores = num_jogadores
	print(sessions[session].nome_equipes)
	return "", 201

@app.route('/get_teams/<int:session>', methods=['GET'])
def get_teams(session):
	if session not in sessions:
		abort(400)
	print(sessions[session])
	return dumps(sessions[session].__dict__)
	
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

@app.route('/get_team_members/<int:session>', methods=['GET'])
def get_team_members(session):
	if session not in sessions:
		abort(400)
	if len(sessions[session].nome_equipes) * sessions[session].num_jogadores * 3 <= len(sessions[session].words):
		return dumps(sessions[session].__dict__), 201
	else:
		return "", 200

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
	return str(len(sessions[session].words) >= num_jogadores * 3), 200	

@app.route('/start_timer/<int:session>', methods=['GET'])
def start_timer(session):
	if session not in sessions:
		abort(400)

	if sessions[session].timestamp == 0:
		sessions[session].timestamp = int(round(time() * 1000))
		return 'true', 200
	else:
		return 'false', 200

@app.route('/get_timer/<int:session>', methods=['GET'])
def get_timer(session):
	if session not in sessions:
		abort(400)

	if sessions[session].timestamp == 0:
		return '0', 200
	else:
		return str(int(round(time() * 1000)) - sessions[session].timestamp).split('.')[0], 200

@app.route('/stop_timer/<int:session>', methods=['POST'])
def stop_timer(session):
	print(session in sessions)
	if not request.json or session not in sessions:
		abort(400)

	sessions[session].timestamp = 0
	sessions[session].words = request.get_json()
	print(sessions[session].words)
	sessions[session].words_version += 1
	return "", 201

@app.route('/get_game_state/<int:session>', methods=['GET'])
def get_game_state(session):
	if session not in sessions:
		abort(400)
	return dumps(sessions[session].__dict__)
 

	return dumps(sessions[session].__dict__), 200
if __name__ == '__main__':
    app.run(debug=True)