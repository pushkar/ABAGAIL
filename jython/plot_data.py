"""
* Example graphs 
*
* @author John Mansfield
* @version 1.0
"""

import os
import matplotlib.pyplot as plt
import pandas as pd

def plot_data(plot_title, df):
    line_colors = ['lime','orchid']
    ax = df.plot(title=plot_title, style=line_colors, lw=1.5, fontsize=11)
    ax.set_xlabel("Iteration")
    ax.set_ylabel("Error")
    fig=ax.get_figure()
    fig.savefig(os.path.join('image', plot_title + '.png'))
    
if __name__ == "__main__":
    plot_title="Neural Net - Train vs Test Error"
    backprop_df = pd.read_csv(os.path.join('data', 'NNOut.csv'))
    RHC_df = pd.read_csv(os.path.join('data', 'RHC-NNOut.csv'))
    SA_df = pd.read_csv(os.path.join('data', 'SA-NNOut.csv'))
    GA_df = pd.read_csv(os.path.join('data', 'GA-NNOut.csv'))
    #df= pd.concat([RHC_df, SA_df, GA_df], axis=1)
    dfs=[backprop_df, RHC_df, SA_df, GA_df]
    nn_names = ['backprop', 'RHC', 'SA', 'GA']
    for i in range(len(nn_names)):
        plot_data(nn_names[i], dfs[i])