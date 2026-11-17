"""Verificações estáticas e de preservação. Não compila nem executa o Android."""
from pathlib import Path
import hashlib
import json
import subprocess
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
results = []

def check(name, action):
    try:
        action()
        results.append({'check': name, 'status': 'PASS'})
    except Exception as e:
        results.append({'check': name, 'status': 'FAIL', 'reason': str(e)})

def ensure(condition, message):
    if not condition:
        raise AssertionError(message)

def xml_check():
    for p in (ROOT/'app/src/main').rglob('*.xml'):
        ET.parse(p)

def manifest_check():
    android = '{http://schemas.android.com/apk/res/android}'
    m = ET.parse(ROOT/'app/src/main/AndroidManifest.xml').getroot()
    perms = {p.get(android+'name') for p in m.findall('uses-permission')}
    ensure('android.permission.POST_NOTIFICATIONS' in perms, 'Permissão ausente')
    ensure('android.permission.SCHEDULE_EXACT_ALARM' not in perms, 'Alarme exato fora do escopo')
    ensure('android.permission.RECEIVE_BOOT_COMPLETED' not in perms, 'Restauração de boot fora do escopo')
    receivers = m.findall('./application/receiver')
    ensure(any(r.get(android+'name') == '.reminders.ReminderReceiver' and r.get(android+'exported') == 'false' for r in receivers), 'Receiver ausente/exportado')

def preservation_check():
    hashes = json.loads((ROOT/'tools/p1-preservacao-sha256.json').read_text())
    for name, expected in hashes.items():
        p = ROOT/name
        # A configuração Firebase é mantida na cópia de trabalho, mas omitida do pacote compartilhável.
        if name.endswith('google-services.json') and not p.exists():
            continue
        ensure(hashlib.sha256(p.read_bytes()).hexdigest() == expected, f'Alteração fora do escopo: {name}')

def wrapper_check():
    p = ROOT/'gradlew'
    ensure(b'\r' not in p.read_bytes(), 'CRLF no script Linux')
    subprocess.run(['bash', '-n', str(p)], check=True)

def source_hash():
    files = sorted(p for p in ROOT.rglob('*') if p.is_file() and '.git' not in p.parts and
                   (p.suffix in {'.kt','.kts','.toml','.xml'} or p.name in {'gradlew','.gitattributes'}))
    digest = hashlib.sha256()
    for p in files:
        digest.update(str(p.relative_to(ROOT)).encode())
        digest.update(p.read_bytes())
    return digest.hexdigest()

check('XMLs do aplicativo bem formados', xml_check)
check('Manifest: permissão e receiver privado; sem alarme exato/boot', manifest_check)
check('Preservação byte a byte de dados, migrações, telas de jogos e dependências P1', preservation_check)
check('Wrapper Linux: LF e sintaxe Bash', wrapper_check)
report = {'scope': 'Somente fontes e configuração. Não comprova compilação nem comportamento Android.',
          'version': 'P2-'+source_hash()[:12], 'source_sha256': source_hash(), 'checks': results}
out = ROOT/'docs/evidencias/verificacao-fontes.json'
out.parent.mkdir(parents=True, exist_ok=True)
out.write_text(json.dumps(report, ensure_ascii=False, indent=2)+'\n')
print(json.dumps(report, ensure_ascii=False, indent=2))
raise SystemExit(1 if any(r['status'] == 'FAIL' for r in results) else 0)
