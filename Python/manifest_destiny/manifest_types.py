
_author__ = 'Nick Cuthbert <nick@whereismytransport.com>'

import json


class FlatType(object):
    def __init__(self,name,proto_file, version="0.0.0"):
        self.name=name[0].upper()+name[1:]
        self.proto_file=proto_file
        self.version=version


class TypeTree(object):
    """A class describing the type tree for protocol buffer files"""

    def __init__(self, name, file, is_type=True, version="0.0.0"):
        self.name = name
        self.children = []
        self.is_type = is_type
        self.version = version
        self.proto_file = file

    def add_child(self, child):
        self.children.append(child)

    def collapse_types(self):
        types = []
        if (self.is_type):
            types.append(FlatType(self.name, self.proto_file, self.version))
        for child in self.children:
            for child_type in child.collapse_types():
                if (self.name != ""):
                    child_name = self.name + '.' + child_type.name
                else:
                    raise IOError("Badly formed proto file. A parent must be named")
                types.append(FlatType(child_name, child_type.proto_file, child_type.version))
        return types

    def __str__(self):
        return "name: " + self.name + ", version: " + self.version + ", protofile: " + self.proto_file


class Manifest(object):
    def __init__(self, proto_path, manifest_path):
        self.manifest_path = manifest_path
        self.proto_path = proto_path
        self.protocolBuffers_old = {}
        self.protocolBuffers = {}
        try:
            with open(manifest_path) as manifest:
                manifest_json = json.load(manifest)
                self.assembly_name = manifest_json["assemblyName"]
                self.version = manifest_json["version"]
                self.description = manifest_json["description"]
                self.friendlyName = manifest_json["friendlyName"]
                for proto_type in manifest_json["protocolBuffers"]:
                    self.protocolBuffers_old[proto_type["fullname"]] = FlatType(proto_type["fullname"],
                                                                                proto_type["protoFile"],
                                                                                proto_type["version"])

                manifest.close()
        except IOError as e:
            self.assembly_name = "RandomAssembly"
            self.version = "0.0.0"
            self.description = "This is a sprig. Sprigs rule"
            self.friendlyName = "Randy Random Assembly"

    def update(self, flat_types):
        for flat_type in flat_types:
            if flat_type.name not in self.protocolBuffers_old:
                self.protocolBuffers[flat_type.name] = flat_type
            else:
                # Only update protofile. Version is only either 0.0.0 or user defined
                self.protocolBuffers[flat_type.name] = FlatType(flat_type.name, flat_type.proto_file,
                                                                self.protocolBuffers_old[flat_type.name].version)

    def export(self):
        try:
            with open(self.manifest_path, "w") as manifest_file:
                manifest = {}
                manifest["description"] = self.description
                manifest["friendlyName"] = self.friendlyName
                manifest["version"] = self.version
                manifest["assemblyName"] = self.assembly_name

                protos = []

                for proto_type in self.protocolBuffers.values():
                    protos.append({"fullname": proto_type.name, "version": proto_type.version,
                                   "protoFile": proto_type.proto_file})

                manifest["protocolBuffers"] = protos
                text = json.dumps(manifest, sort_keys=True, indent=4, separators=(',', ': '))
                manifest_file.write(text)

        except IOError as e:
            print("Cannot export manifest")
            print(e)
