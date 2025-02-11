import random
import sys
from datetime import datetime
from math import floor

now = datetime.now()
from matplotlib.patches import FancyArrowPatch, ArrowStyle
import matplotlib.pyplot as plt
from PIL import Image
n = (len(sys.argv)-2)//3
fig, ax = plt.subplots()
style = ArrowStyle("-|>", head_length=1, head_width=0.5)
delim = sys.argv[1]
counter = 2

plt.axhline(0, color='black')
plt.axvline(0, color='black')
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

name=now.strftime("vectors_%d-%m-%y_%H:%M:%S")
if (len(sys.argv)-2)%3!=0:
    name=sys.argv[counter]
    counter = counter + 1



plt.xlabel('x')
plt.ylabel('y')
ax.set_aspect('equal', adjustable='box')
ax.autoscale_view()
plt.text(0, 0, "0", horizontalalignment='left', wrap=True)
plt.title('Free body diagram of several forces')
yabs_max = abs(max(ax.get_ylim(), key=abs))
ax.set_ylim(ymin=-yabs_max, ymax=yabs_max)
xabs_max = abs(max(ax.get_xlim(), key=abs))
ax.set_xlim(xmin=-xabs_max, xmax=xabs_max)
#plt.grid(True) #Uncomment to add a grid
plt.show()
#plt.savefig("plots"+delim+name,dpi=1200) //for now
sys.exit()