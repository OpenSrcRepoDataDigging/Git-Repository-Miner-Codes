import jpype
import os

jar_path = os.path.join(os.path.abspath('.'),'test.jar')

jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=%s"%jar_path)

MainDataGenerator=jpype.JClass('LaunchFunction.MainDataGenerator')

mdg = MainDataGenerator()

res = mdg.generateNew("https://github.com/MirageLyu/test.git")

print(res)

jpype.shutdownJVM()
