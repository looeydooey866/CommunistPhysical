import random
import sys
from datetime import datetime
from math import floor
from matplotlib.patches import FancyArrowPatch, ArrowStyle
import matplotlib.pyplot as plt
import os

now = datetime.now()
# num of vectors, then vector entries, then modifiers come afterward.
fig, ax = plt.subplots()
style = ArrowStyle("-|>", head_length=1, head_width=0.5)
plt.xlabel('x')
plt.ylabel('y')
ax.set_aspect('equal', adjustable='box')

plt.text(0, 0, "0", horizontalalignment='left', wrap=True)

plt.axhline(0, color='black')
plt.axvline(0, color='black')

n = int(sys.argv[1])
counter = 2
for i in range(n):
    name = sys.argv[counter]
    counter = counter + 1
    x = float(sys.argv[counter])
    counter = counter + 1
    y = float(sys.argv[counter])
    counter = counter + 1
    color = [random.random(),random.random(),random.random()]
    arrow = FancyArrowPatch((0, 0), (x,y), mutation_scale=10, arrowstyle=style, color=color, label=name)
    ax.add_patch(arrow)
    a = ""
    b = ""
    if x >= 0:
        a = "left"
    else:
        a = "right"

    if y >= 0:
        b = "bottom"
    else:
        b = "top"
    plt.text(x,y,name, fontsize=12, color=color, ha=a, va=b)

ax.autoscale_view()
yabs_max = abs(max(ax.get_ylim(), key=abs))
ax.set_ylim(ymin=-yabs_max, ymax=yabs_max)
xabs_max = abs(max(ax.get_xlim(), key=abs))
ax.set_xlim(xmin=-xabs_max, xmax=xabs_max)

title = sys.argv[counter]
counter = counter + 1

plt.title(title)

if sys.argv[counter] == "display":
    plt.show()
    counter = counter + 1
elif sys.argv[counter] == "save":
    counter = counter + 1
    name = ""
    if sys.argv[counter] != "<<NO_NAME>>":
        name = sys.argv[counter]
    else:
        name=now.strftime("vectors_%d-%m-%y_%H:%M:%S")
    plt.savefig("src"+os.sep+"plots"+os.sep+name+".png",dpi=1200)
sys.exit()
