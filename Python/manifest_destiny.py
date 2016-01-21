"""
This program generates (or updates) a OtherTree Sprig's manifest by scanning the supplied path to a folder of protocol
buffers. It proceeds to output this manifest to the given file.

Args:
    arg1 - Path to proto directory
    arg2 - Path to manifest.json. This file will be created if it does not exist
"""

import manifest_generator
import ply
import sys


def main():
    manifest_generator.export_and_generate_manifest(sys.argv[1], sys.argv[2])


if __name__ == "__main__":
    main()
