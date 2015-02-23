import string
import cherrypy
import datetime
import pymongo
import json
from pymongo import MongoClient

class TagAndFragWebService(object):
     client = MongoClient()
     exposed = True
     db = client.test_database
     collection = db.test_collection
     players = db.players
     cherrypy.config.update({'server.socket_host':'158.75.2.62',
			     'server.socket_port':8080})     
     
     
     
     @cherrypy.tools.accept(media='text/plain')
     def GET(self):
         #return cherrypy.session['playername']
        docs = self.db.players.find({} , { '_id': 0})
        docs = list(docs) #to wycignie z bazy
        return json.dumps(docs)
         #return json.dumps(self.db.players.find_one())
         #return output

     def POST(self, player_name):
         name = player_name
         player = { "name": name,
                    "health" : 3,
                    "ammunition" : 5,
                    "localization" : 123}
         self.players.insert(player)
         return "Dodano gracza " + name

     def PUT(self, player_name, hp='default', ammo='default', loc='default'):
         #cherrypy.session['playername'] = another_string
         if hp is not 'default':
            self.db.players.update({'name':player_name}, {"$set":{'health': hp}}, upsert=False)
         if ammo is not 'default':
            self.db.players.update({'name':player_name}, {"$set":{'ammunition': ammo}}, upsert=False)
         if loc is not 'default':
            self.db.players.update({'name':player_name}, {"$set":{'localization': loc}}, upsert=False)

     #def PUT(self, player_name, ammo):
         #cherrypy.session['playername'] = another_string
      #   self.db.players.update({'name':player_name}, {"$set":{'ammunition': ammo}}, upsert=False)

     def DELETE(self):
         #cherrypy.session.pop('playername', None)
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
