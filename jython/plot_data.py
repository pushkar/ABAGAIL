import os
import matplotlib.pyplot as plt
import pandas as pd

def plot_data(plot_title, df):
    line_colors = ['c','lime','orchid']
    ax = df.plot(title=plot_title, style=line_colors, lw=1.5, fontsize=11)
    ax.set_xlabel("Iteration")
    ax.set_ylabel("Error")
    fig=ax.get_figure()
    fig.savefig(os.path.join('image', 'NN-graph1.png'))
    
if __name__ == "__main__":
    plot_title="Neural Net - RO Weight Training"
    RHC_df = pd.read_csv(os.path.join('data', 'RHC.csv'))
    SA_df = pd.read_csv(os.path.join('data', 'SA.csv'))
    GA_df = pd.read_csv(os.path.join('data', 'GA.csv'))
    df= pd.concat([RHC_df, SA_df, GA_df], axis=1)
    df.columns = ['RHC', 'SA', 'GA']
    plot_data(plot_title, df)