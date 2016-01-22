
_author__ = 'Nick Cuthbert <nick@whereismytransport.com>'

import ply.lex as lexer

reserved = {
    'message': 'MESSAGE',
    'package': 'PACKAGE',
}

tokens = ('ID',
          'LEFT_BRACE',
          'RIGHT_BRACE',
          'COLON',
          'MESSAGE',
          'PACKAGE',
          'PACKAGE_ID',
          'COMMENT',
          'FIELD_DELIMITER'
          )

# Regexes
t_COLON = r';'
t_LEFT_BRACE = r'\{'
t_RIGHT_BRACE = r'\}'

t_FIELD_DELIMITER = r'\W*=\W*[0-9]+\W*;'

t_COMMENT = r'((//.*(\n|\r\n))|(/\*(.|\n|\r\rn)*\*/))'


def to_pascal_case(val):
    words = val.split('.')
    new_words = ""
    for word_count in range(len(words)):
        if(len(words[word_count])>0):
            words[word_count]=words[word_count][0].upper()+words[word_count][1:]
            if(word_count!=0):
                new_words+="."+words[word_count]
            else:
                new_words = words[word_count]
    return new_words


def t_ID(t):
    r'[a-zA-Z_][(.)?a-zA-Z_0-9]*'
    if ("." in t.value):
        t.type = "PACKAGE_ID"
        t.value = to_pascal_case(t.value)
    else:
        t.type = reserved.get(t.value, 'ID')  # Check for reserved words
    return t


def t_error(t):
    t.lexer.skip(1)
    t.lineno = t.lexer.lineno
    return t


lexer.lex()
