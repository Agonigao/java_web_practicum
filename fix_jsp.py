import os
import re
directory = r"C:\Users\dell\IdeaProjects\java_web_practicum\web\WEB-INF\views"
out_directory = r"C:\Users\dell\IdeaProjects\java_web_practicum\out\artifacts\java_web_practicum_Web_exploded\WEB-INF\views"
replacements = {
    r'吕梁学院资料室管理系\X?\s*</title>': r'吕梁学院资料室管理系统</title>',
    r'吕梁学院资料室管理系\X\s*-': r'吕梁学院资料室管理系统 -',
    r'吕梁学院资料室读\X?\s*</p>': r'吕梁学院资料室读者</p>',
    r'用户\X?\s*\*\s*</label>': r'用户名 *</label>',
    r'用户的账\X?\s*>': r'用户的账号">',
    r'登录的账\X?\s*>': r'登录的账号">',
    r'真实姓\X?\s*>': r'真实姓名">',
    r'密\X?\s*>': r'密码">',
    r'读者类\X?\s*\*\s*</label>': r'读者类型 *</label>',
    r'本科\X?\s*</option>': r'本科生</option>',
    r'所在院\X?\s*\*\s*</label>': r'所在院系 *</label>',
    r'手机号\X?\s*>': r'手机号码">',
    r'>\s*\X?\X?\?\s*</button>': r'>注 册</button>',
    r'添加图\X?\s*</p>': r'添加图书</p>',
    r'编辑、删除图\X?\s*</p>': r'编辑、删除图书</p>',
    r'书\X?\s*>': r'书名">',
    r'作\X?\s*\*\s*</label>': r'作者 *</label>',
    r'输入作\X?\s*>': r'输入作者">',
    r'出版\X?\s*\*\s*</label>': r'出版社 *</label>',
    r'总数\X?\s*\*\s*</label>': r'总数量 *</label>',
    r'可借数\X?\s*\*\s*</label>': r'可借数量 *</label>',
    r'返回控制\X?\s*</a>': r'返回控制台</a>',
    r'>\s*退\X?\s*</a>': r'>退出</a>',
    r'作\X?\s*</th>': r'作者</th>',
    r'出版\X?\s*</th>': r'出版社</th>',
    r'确认删除\X?\s*\)': r'确认删除吗？)',
    r'管理\X?\s*</h1>': r'管理员</h1>',
    r'控制\X?\s*</a>': r'控制台</a>',
    r'读者管\X?\s*</a>': r'读者管理</a>',
    r'读者管\X?\s*</h4>': r'读者管理</h4>',
    r'欢迎\X?\s*\$\{(sessionScope|user)': r'欢迎您，${\1',
    r'欢迎回来\X?\s*\$\{sessionScope': r'欢迎回来，${sessionScope',
    r'快速操\X?\s*</h3>': r'快速操作</h3>',
    r'户账号和权\X?\s*</p>': r'户账号和权限</p>',
    r'<h1>\X?归还记录': r'<h1>📋 归还记录',
    r'所有归还记\X?\s*</h2>': r'所有归还记录</h2>',
    r'读\X?\s*</th>': r'读者</th>',
    r'用户\X?\s*</th>': r'用户名</th>',
    r'已归\X?\s*</span>': r'已归还</span>',
    r'借阅\X?\s*</span>': r'借阅中</span>',
    r'>\s*借\X?\s*</a>': r'>借阅</a>',
    r'>\s*续\X?\s*</a>': r'>续借</a>',
    r'我的借阅记\X?\s*</h1>': r'我的借阅记录</h1>',
    r'输入书名、作者或ISBN搜\X?\s*\.\.\.">': r'输入书名、作者或ISBN搜索...">',
    r'学号或工号\X?\s*>': r'学号或工号">',
    r'图书类\X?\s*</option>': r'图书类型</option>',
    r'状\X?\s*</th>': r'状态</th>',
    r'操作\X?\s*</th>': r'操作</th>',
    r'续借次\X?\s*</th>': r'续借次数</th>',
    r'信息更新\X?功\X?</div>': r'信息更新成功！</div>',
    r'个人信\X?\s*</h1>': r'个人信息</h1>',
    r'所在院\X?\s*</label>': r'所在院系</label>',
    r'吕梁学院资料\X?\s*</title>': r'吕梁学院资料室管理系统</title>',
    r'吕梁学院资料\X?\s*</h1>': r'吕梁学院资料室管理系统</h1>',
    r'提醒（未\X?天内\X?\s*</h3>': r'提醒（未来3天内）</h3>',
    r'提醒（未来3天内\X?</h3>': r'提醒（未来3天内）</h3>',
    r'添加\X?\s*</a>': r'添加</a>',
    r'>\X?\X?\s*</button>': r'>注 册</button>',
    r'>\X?\X?</span>': r'>可用</span>',
    r'\'\X?\s*:\s*\'\X?\}</td>': r"'是' : '否'}</td>",
    r'\?\s*:\s*\'\X?\}</td>': r"? '是' : '否'}</td>",
    r'所有\X?还记\X?</h2>': r'所有归还记录</h2>',
    r'图书\X?理</a': r'图书管理</a',
    r'控制\X?': r'控制台',
    r'<th>库存</\X?': r'<th>库存</th>',
    r'<th>\X?量</th>': r'<th>数量</th>',
    r'理</a>': r'管理</a>',
    r'读\X?</h1>': r'读者</h1>'
}
def fix_file(filepath):
    if not filepath.endswith('.jsp') or 'login.jsp' in filepath:
        return
    with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
        content = f.read()
    # 额外精确字符串替换
    content = content.replace('🏷?图书类型管理', '🏷 图书类型管理')
    content = content.replace(' 快速操?</h3>', '🔧 快速操作</h3>')
    content = content.replace(' 归还记录', '📋 归还记录')
    content = content.replace('管理系? - 管理?</h1>', '管理系统 - 管理员</h1>')
    content = content.replace('吕梁学院资料室管理系?</h1>', '吕梁学院资料室管理系统</h1>')
    content = content.replace('已有账号?a', '已有账号？<a')
    content = content.replace('图书列? <a', '图书列表 <a')
    content = content.replace('管理系? - 读?</h1>', '管理系统 - 读者</h1>')
    content = content.replace('录</button>', '录</button>')
    content = content.replace('图书列?</h2>', '图书列表</h2>')
    content = content.replace('读者列?</h2>', '读者列表</h2>')
    content = content.replace('类?</p>', '类型</p>')
    content = content.replace('系电话</label>', '联系电话</label>')
    content = content.replace('图书加?</p>', '图书类型</p>')
    content = content.replace('添加图?</p>', '添加图书</p>')
    content = content.replace('户账号和权?</p>', '用户账号和权限</p>')
    content = content.replace('?归还记录', '📋 归还记录')
    content = content.replace('读者管?</a>', '读者管理</a>')
    # 再次使用 Regex 的备用清洗
    content = content.replace('', '')
    for pat, rep in replacements.items():
        content = re.sub(pat, rep, content)
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
for root_dir in [directory, out_directory]:
    for root, dirs, files in os.walk(root_dir):
        for file in files:
            fix_file(os.path.join(root, file))
print("Fixed JSPs via python.")
