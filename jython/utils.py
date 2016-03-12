from itertools import *

# given an iterable of pairs return the key corresponding to the greatest value
def argmax(pairs):
    return max(pairs, key=itemgetter(1))[0]

# given an iterable of values return the index of the greatest value
def argmax_index(values):
    return argmax(enumerate(values))

# given an iterable of keys and a function f, return the key with largest f(key)
def argmax_f(keys, f):
    return max(keys, key=f)
