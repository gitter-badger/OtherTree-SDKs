import os

from proto_parser import *


def strip_proto(name):
    index=str(name).rindex(".proto")
    return str(name)[:index]

def export_and_generate_manifest(proto_folder,manifest_file):
    manifest=Manifest(proto_folder,manifest_file)

    for root, subFolders, files in os.walk(proto_folder):
        for file in files:
            file_path = os.path.join(root, file)
            with open(file_path,'r') as proto_file:
                source=proto_file.read()
                proto_file.close()
                types=parseProto(source,strip_proto(file)).collapse_types()
                manifest.update(types)
    manifest.export()
