
_author__ = 'Nick Cuthbert <nick@whereismytransport.com>'
import ply.yacc as yacc
from .proto_lexer import *
from .manifest_types import *

start = 'start'


def p_root(t):
    '''start    : package
                | messages'''
    t[0] = t[1]


def p_package(t):
    '''package  : PACKAGE ID COLON messages
                | PACKAGE PACKAGE_ID COLON messages'''
    t[0] = TypeTree(t[2], proto_file, is_type=False)
    t[0].children = t[4]


def p_messages(t):
    '''messages     : nested_message
                    | nested_message messages'''
    if (t[0] is None):
        t[0] = []
    t[0].append(t[1])

    if (len(t) > 2 and type(t[2]) is list):
        for typ in t[2]:
            if (type(typ) is TypeTree):
                t[0].append(typ)


def p_nested_message(t):
    '''nested_message  : MESSAGE ID LEFT_BRACE message_contents'''

    t[0] = TypeTree(t[2], proto_file)

    if (type(t[4]) is list):
        t[0].children = t[4]
    else:
        t[0].children = [t[4]]


def p_message_contents(t):
    '''message_contents : nested_message message_contents
                        | field message_contents
                        | RIGHT_BRACE'''

    if (t[0] is None):
        t[0] = []

    if (len(t) > 1 and type(t[1]) is TypeTree):
        t[0].append(t[1])

    if (len(t) > 2):
        if (type(t[2]) is list):
            for t_type in t[2]:
                if (type(t_type) is TypeTree):
                    t[0].append(t_type)


def p_field(t):
    '''field    : ID ID FIELD_DELIMITER
                | ID MESSAGE FIELD_DELIMITER
                | ID PACKAGE FIELD_DELIMITER'''
    pass


def p_error(p):
    if (p is None):
        parser.error = "eof"
    else:
        parser.errok()


parser = yacc.yacc()


def parseProto(protobuf_file, file_location):
    global proto_file
    proto_file = file_location
    return parser.parse(protobuf_file)
