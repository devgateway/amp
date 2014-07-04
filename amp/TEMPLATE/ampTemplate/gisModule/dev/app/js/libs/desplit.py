"""
    usage:

        desplit a single optimized AMD module:

            ./desplit.py some-compressed-amd-module.js

        desplit a bunch of optimized AMD modules recursively:

            find . -name '*.js' | xargs python desplit.py

    author: Phil Schleihauf
"""


import re


splitter_re = r'define\("(?P<name>[^"]*)","(?P<reqs>[^"]*)".split\(" "\)'
sub_with = 'define("{name}",[{quoted_reqs}]'


def split_to_arr(match):
    stuff = match.groupdict()
    quoted_reqs = ','.join(map('"{}"'.format, stuff['reqs'].split(' ')))
    return sub_with.format(name=stuff['name'], quoted_reqs=quoted_reqs)


desplitter = lambda source: re.subn(splitter_re, split_to_arr, source)


stats = {
  'desplit': 0,
  'skipped': 0,
  'errors': 0,
}


def desplit_f(filename):
    with open(filename) as f:
        source = f.read()
    desplit, splits = desplitter(source)
    if splits == 0:
        stats['skipped'] += 1
        return
    elif splits == 1:
        with open('{}.not.desplit'.format(filename), 'w') as backup:
            backup.write(source)
        with open(filename, 'w') as f:
            f.write(desplit)
        stats['desplit'] += 1
    else:
        stats['errors'] += 1


if __name__ == '__main__':
    import sys
    filenames = sys.argv[1:]
    [desplit_f(f) for f in filenames]
    print('desplit: {desplit}\n'
          'skipped: {skipped}\n'
          'errors:  {errors}'.format(**stats))
