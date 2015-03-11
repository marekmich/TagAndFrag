import cherrypy
import json
import random
from pymongo import MongoClient
from datetime import datetime

def getRandomId():
	id = random.randint(1000000,9999999)
	return id


class TagAndFragWebService(object):
     client = MongoClient()
     exposed = True
     db = client.test_database
     collection = db.test_collection
     players = db.players
     cherrypy.config.update({'server.socket_host' : '158.75.2.62',
			     'server.socket_port' : 8080,
			     'log.error_file' : 'error_log',     
			     'log.access_file' : 'access_log',
			     'log.screen' : True})
     log = open('fraglog','a')
     
     @cherrypy.tools.accept(media='text/plain')

     def GET(self):
         docs = self.db.players.find({} , { '_id': 0})
         docs = list(docs) #to wyciagnie z bazy
         return json.dumps(docs)
	 #id = 1762279
	 #name = "wojtek"
	 #return str(self.db.players.find_one({"id" : id, "name" : name }))
	 #return str(self.db.players.find_one({"id" : 1762278, "name" : "wojtek"}))

     def POST(self, player_name, team):
         name = player_name
	 id = getRandomId()
         player = { "id" : id,
                    "name" : name,
		    "team" : team,
                    "health" : 100,
                    "ammunition" : 100,
                    "localization" : 123}
         self.players.insert(player)
         return str(id)

     def PUT(self, id, player_name, attacker_name, hp='default', ammo='default', loc='default'): 
	 #log = open('fraglog','w')
	 id = int(id)
         cursor =  self.db.players.find({"id" : id, "name" : player_name })
	 count = str(cursor.count()) 
	 if count is not "0":
		attacker_hp = self.db.players.find({"name" : attacker_name})
		for field in attacker_hp:
		    att_hp = field['health']
		player_hp = self.db.players.find({"name" : player_name})
		for field in player_hp:
		    pla_hp = field['health']
		#pla_hp = int(pla_hp)-1
		TagAndFragWebService.log.write(str(datetime.now()) + " " + attacker_name + "(" + str(att_hp) + ")" +  " trafil " + player_name + "(" + str(pla_hp) + ")" + "\n")

       		if hp is not 'default':
            		self.db.players.update({'name':player_name}, {"$set":{'health': hp}}, upsert=False)
         	if ammo is not 'default':
           		self.db.players.update({'name':player_name}, {"$set":{'ammunition': ammo}}, upsert=False)
         	if loc is not 'default':
            		self.db.players.update({'name':player_name}, {"$set":{'localization': loc}}, upsert=False)
	 else:
		return "id gracza niezgodne z name"
	
     def DELETE(self):
         self.db.players.remove({})

if __name__ == '__main__':
     conf = {
         '/': {
             'request.dispatch': cherrypy.dispatch.MethodDispatcher(),
             'tools.sessions.on': True,
             'tools.response_headers.on': True,
             'tools.response_headers.headers': [('Content-Type', 'text/plain')],
         }
     }
     cherrypy.quickstart(TagAndFragWebService(), '/', conf)
