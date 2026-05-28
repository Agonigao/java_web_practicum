$replacements = @{
    '管理系[\?]?\s*</title>' = '管理系统</title>'
    '管理系[\?]?\s*-' = '管理系统 -'
    '资料室读[\?]?\s*</p>' = '资料室读者</p>'
    '用户[\?]?\s*\*\s*</label>' = '用户名 *</label>'
    '户的账[\?]?\s*>' = '户的账号">'
    '录的账[\?]?\s*>' = '录的账号">'
    '真实姓[\?]?\s*>' = '真实姓名">'
    '密[\?]?\s*>' = '密码">'
    '读者类[\?]?\s*\*\s*</label>' = '读者类型 *</label>'
    '本科[\?]?\s*</option>' = '本科生</option>'
    '所在院[\?]?\s*\*\s*</label>' = '所在院系 *</label>'
    '手机号[\?]?\s*>' = '手机号码">'
    '>\s*[注\?]\s*[册\?]\s*</button>' = '>注 册</button>'
    '添加图[\?]?\s*</p>' = '添加图书</p>'
    '删除图[\?]?\s*</p>' = '删除图书</p>'
    '书[\?]?\s*>' = '书名">'
    '作[\?]?\s*\*\s*</label>' = '作者 *</label>'
    '输入作[\?]?\s*>' = '输入作者">'
    '出版[\?]?\s*\*\s*</label>' = '出版社 *</label>'
    '总数[\?]?\s*\*\s*</label>' = '总数量 *</label>'
    '可借数[\?]?\s*\*\s*</label>' = '可借数量 *</label>'
    '控制[\?]?\s*</a>' = '控制台</a>'
    '>\s*退[\?]?\s*</a>' = '>退出</a>'
    '作[\?]?\s*</th>' = '作者</th>'
    '出版[\?]?\s*</th>' = '出版社</th>'
    '确认删除[\?]?\s*\)' = '确认删除吗？)'
    '>\s*删除[\?]?\s*</a>' = '>删除</a>'
    '管理[\?]?\s*</h1>' = '管理员</h1>'
    '读者管[\?]?\s*</a>' = '读者管理</a>'
    '读者管[\?]?\s*</h4>' = '读者管理</h4>'
    '欢迎[\?]?\s*\$\{(sessionScope|user)' = '欢迎您，${$1'
    '回来[\?]?\s*\$\{sessionScope' = '回来，${sessionScope'
    '快速操[\?]?\s*</h3>' = '快速操作</h3>'
    '和权[\?]?\s*</p>' = '和权限</p>'
    '>[\?]?归还记录' = '>📋 归还记录'
    '归还记[\?]?\s*</h2>' = '归还记录</h2>'
    '读[\?]?\s*</th>' = '读者</th>'
    '用户[\?]?\s*</th>' = '用户名</th>'
    '已归[\?]?\s*</span>' = '已归还</span>'
    '借阅[\?]?\s*</span>' = '借阅中</span>'
    '>\s*借[\?]?\s*</a>' = '>借阅</a>'
    '>\s*续[\?]?\s*</a>' = '>续借</a>'
    '借阅记[\?]?\s*</h1>' = '借阅记录</h1>'
    'ISBN搜[\?]?\s*\.\.\.">' = 'ISBN搜索...">'
    '或工号[\?]?\s*>' = '或工号">'
    '图书类[\?]?\s*</option>' = '图书类型</option>'
    '状[\?]?\s*</th>' = '状态</th>'
    '操作[\?]?\s*</th>' = '操作</th>'
    '借次[\?]?\s*</th>' = '借次数</th>'
    '新[\?]功[\?]?</div>' = '新成功！</div>'
    '个人信[\?]?\s*</h1>' = '个人信息</h1>'
    '所在院[\?]?\s*</label>' = '所在院系</label>'
    '室管理系[\?]?\s*</title>' = '室管理系统</title>'
    '室管理系[\?]?\s*</h1>' = '室管理系统</h1>'
    '（未[\?]天内[\?]?\s*</h3>' = '（未来3天内）</h3>'
    '（未来3天内[\?]?\s*</h3>' = '（未来3天内）</h3>'
    '添加[\?]?\s*</a>' = '添加</a>'
    '>[\?][\?]</span>' = '>可用</span>'
    '''[\?]?\s*:\s*''[\?]?\}</td>' = "'是' : '否'}</td>"
    '\?\s*:\s*''[\?]?\}</td>' = "? '是' : '否'}</td>"
    '所有[\?]还记[\?]?\s*</h2>' = '所有归还记录</h2>'
    '图书[\?]理\s*</a' = '图书管理</a'
    '<th>库存</[\?]?' = '<th>库存</th>'
    '<th>[\?]?量</th>' = '<th>数量</th>'
    '读[\?]?\s*</h1>' = '读者</h1>'
    '学号[\?]?\s*</th>' = '学号</th>'
    '>[\?]?  </button>' = '>注 册</button>'
    '>[\s\?]*</button>' = '>注 册</button>'
}
$dirs = @('C:\Users\dell\IdeaProjects\java_web_practicum\web\WEB-INF\views', 'C:\Users\dell\IdeaProjects\java_web_practicum\out\artifacts\java_web_practicum_Web_exploded\WEB-INF\views')
foreach ($dir in $dirs) {
    if (Test-Path $dir) {
        $files = Get-ChildItem -Path $dir -Recurse -Filter *.jsp
        foreach ($f in $files) {
            if ($f.Name -eq 'login.jsp') { continue }
            $bytes = [System.IO.File]::ReadAllBytes($f.FullName)
            if ($bytes.Length -gt 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
                $bytes = $bytes[3..($bytes.Length-1)]
            }
            $mangledString = [System.Text.Encoding]::UTF8.GetString($bytes)
            $gbkBytes = [System.Text.Encoding]::GetEncoding(936).GetBytes($mangledString)
            $cleanStr = [System.Text.Encoding]::UTF8.GetString($gbkBytes)
            $cleanStr = $cleanStr -replace '🏷\?图书类型管理', '🏷 图书类型管理'
            $cleanStr = $cleanStr -replace ' 快速操\?</h3>', '🔧 快速操作</h3>'
            $cleanStr = $cleanStr -replace '\[\?\]归还记录', '📋 归还记录'
            $cleanStr = $cleanStr -replace '图书加\?</p>', '图书类型</p>'
            $cleanStr = $cleanStr -replace '图书列\? <a', '图书列表 <a'
            $cleanStr = $cleanStr -replace '图书列\?</h2>', '图书列表</h2>'
            $cleanStr = $cleanStr -replace '读者列\?</h2>', '读者列表</h2>'
            $cleanStr = $cleanStr -replace '类\?</p>', '配置</p>'
            $cleanStr = $cleanStr -replace '已有账号\?a', '已有账号？<a'
            foreach ($k in $replacements.Keys) {
                $cleanStr = [regex]::Replace($cleanStr, $k, $replacements[$k])
            }
            $cleanStr = $cleanStr -replace 'http://java.sun.com/jsp/jstl/core', 'jakarta.tags.core'
            Set-Content -Path $f.FullName -Value $cleanStr -Encoding UTF8
        }
    }
}
Write-Host "Done!"
