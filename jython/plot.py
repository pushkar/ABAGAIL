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
            'RHC': [
                0: {
                    'training': [
                        0:{
                            'time',
                            'training_error',
                            'test_error',
                        },
                        ...
                    ],
                    'result': {
                        'correct_classifications'
                        'incorrect_classifications'
                    }
                    'parameter': {<parameters>}
                }
            'SA',
            'GA',
        },
        'train_iteration':{
            'RHC': {
                'training': [
                    0:{
                        'time',
                        'training_error',
                        'test_error',
                    },
                    ...
                ],
                'result': {
                    'correct_classifications'
                    'incorrect_classifications'
                }
            }
            'SA': {
            }
            'GA': {
            }
        }
    }
    """
    """
    task 1:
        plot the test_error curve for the different configurations of algorithms
        for oa in oas
            for config in configurations
                plot a curve
            save a graph
    task 2:
        plot the train/val error against time
    """
    for oa_name in oa_names:
        for config in log['find_parameters'][oa_name]:
            x = [iteration['time'] for iteration in config['training']]
            y = [iteration['test_error'] for iteration in config['training']]
            plt.plot(x, y, label=str(config['parameter']))
        plt.xlabel('time in seconds')
        plt.ylabel('test error')
        plt.title('configurations of {}'.format(oa_name))
        plt.legend()
        plt.savefig('credit-g-find-parameters-{}.png'.format(oa_name))
        plt.clf()

    for oa_name in oa_names:
        for config in log['train_iteration'][oa_name]:
            x = [iteration['time'] for iteration in log['train_iteration'][oa_name]['training']]
            y1 = [iteration['training_error'] for iteration in log['train_iteration'][oa_name]['training']]
            y2 = [iteration['test_error'] for iteration in log['train_iteration'][oa_name]['training']]
            plt.plot(x, y1, label='{} {}'.format(oa_name, 'training')))
            plt.plot(x, y2, label='{} {}'.format(oa_name, 'validation')))
        plt.xlabel('time in seconds')
        plt.ylabel('error')
        plt.title('comparison of train/val error against time'
        plt.legend()
        plt.savefig('credit-g-train-iteration.png'.format(oa_name))
        plt.clf()

if __name__ == "__main__":
    json_file = sys.argv[1]
    with open(json_file, 'r') as f:
        main(json.loads(f.read()))
