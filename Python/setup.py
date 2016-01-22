import re
from setuptools import setup
#import py2exe


with open("README.md", "rb") as f:
    long_descr = f.read().decode("utf-8")

version = re.search(
    '^__version__\s*=\s*"(.*)"',
    open('manifest_destiny/manifest_destiny.py').read(),
    re.M
    ).group(1)

#py2exe.patch_distutils()


setup(
      name='ManifestDestiny',
      packages=['manifest_destiny','ply'],
      version=version,
      author='Nick Cuthbert',
      author_email='nick@whereismytransport.com',
      description='A tool to read in a folder of proto files and automatically update or generate an othertree manifest file',
      url='http://othertree.org',
      entry_points = {
        "console_scripts": ['manifest_destiny = manifest_destiny.manifest_destiny:main']
      },
      long_descr=long_descr,
      package_data={'manifest_destiny':['./manifest_destiny/parser.out']}
    )
