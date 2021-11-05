import sys
import subprocess
from subprocess import Popen, PIPE

TIMEOUT = 100
# Runner for elasticurl integration tests
elasticurl_args = sys.argv[1:]

mvn_args = " ".join(elasticurl_args)

java_command = ['mvn', '-e', 'exec:java', '-Dexec.classpathScope=\"test\"',
                '-Dexec.mainClass=\"software.amazon.awssdk.crt.test.Elasticurl\"', '-Dexec.args=\"{}\"'.format(mvn_args)]

print(java_command)

command_string = " ".join(java_command)
print(command_string)


def run_command(args_str):
    # gather all stderr and stdout to a single string that we print only if things go wrong
    process = subprocess.Popen(
        args_str, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True)
    timedout = False
    try:
        output = process.communicate(timeout=TIMEOUT)[0]
    except subprocess.TimeoutExpired:
        timedout = True
        process.kill()
        output = process.communicate()[0]
    finally:
        if process.returncode != 0 or timedout:
            print(args_str)
            for line in output.splitlines():
                print(line.decode())
            if timedout:
                raise RuntimeError("Timeout happened after {secs} secs from: {cmd}".format(
                    secs=TIMEOUT, cmd=args_str))
            else:
                raise RuntimeError("Return code {code} from: {cmd}".format(
                    code=process.returncode, cmd=args_str))


run_command(command_string)