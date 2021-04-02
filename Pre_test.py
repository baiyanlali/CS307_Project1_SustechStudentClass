# -*- coding: utf-8 -*-
"""
Created on Wed Mar 31 14:58:52 2021

@author: 11911627 Tan Sixu
"""

import re


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
    
    return final


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

test="(化学原理 或者 化学原理 A) 并且 (高等数学（下) 或者 高等数学（下)A 或者 数学分析II)"
t=encode(test)
courses=get_course_name(test)
print(t)
print(courses)

flag=check_satisfy(t,courses,[0,1])
print(flag)
