import os


def dfs_showdir(path, depth):
    if depth == 0:
        print("root:[" + path + "]")

    for item in os.listdir(path):
        if item in ['.git', '.idea', '__pycache__', '.venv']:
            continue

        print("| " * depth + "+--" + item)

        new_item = os.path.join(path, item)  # 使用 os.path.join 以便于跨平台
        if os.path.isdir(new_item):
            dfs_showdir(new_item, depth + 1)


if __name__ == '__main__':
    dfs_showdir('..', 0)