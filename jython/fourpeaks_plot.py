import os
import seaborn as sns
import matplotlib.pyplot as plt
import pandas as pd

fourPeaksDF = pd.read_csv(os.path.join('data', 'MIMIC-fourpeaks.csv'))
sns.set_theme(style="whitegrid")

#fitness v. iters
plt.clf()
sns_plot=sns.lineplot(x="iters", y="fitness", data=fourPeaksDF,)
fig=sns_plot.get_figure()
fig.savefig(os.path.join('image', 'graph1.png'))

#fitness v. fevals
plt.clf()
sns_plot2=sns.lineplot(x="fevals", y="fitness", data=fourPeaksDF,)
fig2=sns_plot2.get_figure()
fig2.savefig(os.path.join('image', 'graph2.png'))

#fevals/iters v. fitness
plt.clf()
sns_plot3=sns.lineplot(x="fitness", y="value", hue="variable", data=pd.melt(fourPeaksDF, ['fitness']), legend="full")
fig3=sns_plot3.get_figure()
fig3.savefig(os.path.join('image', 'graph3.png'))