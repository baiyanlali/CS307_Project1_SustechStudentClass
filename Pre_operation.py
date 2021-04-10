# -*- coding: utf-8 -*-
"""
Created on Fri Apr  2 11:01:30 2021

@author: 11911627 Tan Sixu
"""

import re
import pandas as pd
import csv
import numpy as np


def encode(raw_pre):
    """
    Parameters
    ----------
    raw_pre : TYPE
        raw_pre is a string that contain the raw infomation of pre

    Returns
    -------
    final : TYPE
        return the encoded format of pre, with ease to do logic calculation to check pre

    """
    
    raw_pre=re.sub(r"（","(", raw_pre)     #change to standard ()
    raw_pre=re.sub(r"）",")", raw_pre)

    discard=re.sub(r"[(][^(\(|\))]{,9}[)]","-",raw_pre)   #remove all inner () and set flag
    
    discard=re.sub(r"\s","",discard)            #remove space, save relational ()
    
    remove_all_p=noP=re.sub(r"\(|\)", "",discard)   #remove all () to split couse name
    names=re.split("或者|并且", remove_all_p)     #get names
    
    final=discard
    
    for name in names:
        final=re.sub(name, "%d", final, count=1)          #change all names to %d
    
    
    final=re.sub("或者"," or ", final)                #change to or and
    final=re.sub("并且", " and ", final)
    
    return final, len(names)


def get_course_name(raw_pre):
    """
    This function is used to get list of name of courses, in standard form(without any "()") 

    Parameters
    ----------
    raw_pre : Str
        DESCRIPTION.

    Returns
    -------
    coursese : List
        list of course names

    """
    raw_pre=re.sub(r"（","(", raw_pre)     #change to standard ()
    raw_pre=re.sub(r"）",")", raw_pre)
    
    clean_pre=re.sub(r"\(|\)|\s", "", raw_pre)     #remove all () both inner and outer or space
    
    courses=re.split("或者|并且", clean_pre)
    return courses


def check_satisfy(encoded, pre_list, satified_list):
    """
    THIS function check whether pre are satisfied.

    Parameters
    ----------
    encoded : TYPE
        DESCRIPTION.
    pre_list : TYPE
        DESCRIPTION.
    satified_list : TYPE
        DESCRIPTION.

    Returns
    -------
    TYPE
        Note that to get local var, locals() needed.

    """
    logic=[0 for i in range(len(pre_list))]     #initialize logical list
    
    for index in satified_list:               # flag satisfied courses index as 1
        logic[index]=1
        

    loc=locals()                               #this very tricky
    expression=f"satisfied= {encoded}"%tuple(logic)     #replace
    
    exec(expression)
    
    return loc['satisfied']



if __name__=='__main__':
    pre=open("Pre.csv", 'r',encoding='utf-8')

    with pre:
        tt=pd.read_csv(pre, names=['course','pre'])
        
        
    pattern=[]
    lenth=[]
    courses=[]
    nums=[]
    cid=[]
    
    raw_id=tt['course']
    raws=tt['pre']
    
    raws[raws.isnull()]=0
        
    
    for k in range(len(raws)):
        c=raw_id[k]
        r=raws[k]
        if r==0:
            pattern.append(nan)
            lenth.append(0)
            continue
        tmp_p, tmp_len=encode(r)
        pattern.append(tmp_p)
        lenth.append(tmp_len)
        tmp=get_course_name(r)
        for i in range(len(tmp)):
            cid.append(c)
            courses.append(tmp[i])
            nums.append(i)
            
    
    # pattern={'encode': pattern}
    # lenth={'len': lenth}
    
    # a=pd.DataFrame(pattern)
    # b=pd.DataFrame(lenth)
    
    # tt=tt.append(a, axis=1)
    # tt=tt.append(b,axis=1)
    code=np.array(tt['course']).tolist()
    
    df=pd.DataFrame({'course':code, 'encode':pattern,'len':lenth})
    df.to_csv('encode.csv',index=False, header=False)
    
    df2=pd.DataFrame({'id':cid, 'pre':courses, 'num':nums})
    df2.to_csv('pre_course.csv', index=False, header=False,encoding='utf_8_sig')
    
    
    