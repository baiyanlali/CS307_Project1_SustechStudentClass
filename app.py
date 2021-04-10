# -*- coding: utf-8 -*-
"""
Created on Sat Apr 10 16:35:09 2021

@author: 11911627 Tan Sixu
"""

from flask import Flask
from flask import escape, url_for,request, jsonify
import psycopg2 as psy

def info(sid):
    
    db=psy.connect(database='CS307_SustechStudentClass',user='byll',password='123456',host='10.17.118.214',port='5432')
    cur=db.cursor()
    cur.execute("set search_path = 'Public'")
    cur.execute("""select name, gender, college, course_id
                from student s
                join coursedone c on s.student_id = c.student_id
                where s.student_id='%s' """%sid)
    rows=cur.fetchall()
                
    return rows

app = Flask(__name__)

@app.route('/')
def hello():
    return "hello world again!"

@app.route('/<name>')
def push_name(name):
    return 'user: %s'% escape(name)

@app.route('/student_login/',methods=['GET','POST'])
def test():
    print("value:")
    sid=request.args.get('sid')
    tt=info(sid)
    tt="%s(%s)"%('success', tt)
    return tt
    # return 'success({"totalCapacity": 28})'






if __name__ == '__main__':
    app.run('10.17.70.0',8000)


