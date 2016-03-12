import json
import matplotlib.pyplot as plt
import sys

oa_names = ['RHC', 'SA', 'GA']

def plot(log):
    """
    plot some graphs:
        1. time v.s. iter
        2. train/val err v.s. iter
    """
    """ calculate the training iterations """
    training_iterations = 0
    for item in log:
        if item['type'] == 'training' and item['iteration'] > training_iterations:
            training_iterations = item['iteration']
    training_iterations += 1

    x = range(training_iterations)
    y = {x:{y:[0 for z in xrange(training_iterations)] for y in ('training_error', 'test_error', 'time')} for x in oa_names}
    #print y
    for item in log:
        if item['type'] == 'training':
            y[item['algorithm']]['training_error'][item['iteration']] = item['training_error']
            y[item['algorithm']]['test_error'][item['iteration']] = item['test_error']
            y[item['algorithm']]['time'][item['iteration']] = item['time']
    """time"""
    for oa_name in oa_names:
        plt.plot(x, y[oa_name]['time'], label='{}'.format(oa_name))
    plt.xlabel('iterations')
    plt.ylabel('time')
    plt.title('time v.s. iterations')
    plt.legend()
    plt.savefig('credit-g-time.png')
    plt.clf()
    """error"""
    for oa_name in oa_names:
        plt.plot(x, y[oa_name]['training_error'], label='{} train'.format(oa_name))
        plt.plot(x, y[oa_name]['test_error'], label='{} test'.format(oa_name))
    plt.xlabel('iterations')
    plt.ylabel('error')
    plt.title('error v.s. iterations')
    plt.legend()
    plt.savefig('credit-g-error.png')
    plt.clf()

def main(log):
    """
    log: {
        'find_parameters':{
            'RHC',
            'SA',
            'GA',
        },
        'train_iteration'
    }
    """

if __name__ == "__main__":
    json_file = sys.argv[1]
    with open(json_file, 'r') as f:
        main(json.loads(f.read()))
