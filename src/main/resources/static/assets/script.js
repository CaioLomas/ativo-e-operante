const API = '';
const TOKEN_KEY = 'ativo_token';
const USER_KEY = 'ativo_user';

function getToken() { return localStorage.getItem(TOKEN_KEY); }
function setToken(t) { localStorage.setItem(TOKEN_KEY, t); }
function getUser() { return JSON.parse(localStorage.getItem(USER_KEY) || 'null'); }
function setUser(u) { localStorage.setItem(USER_KEY, JSON.stringify(u)); }
function logout() { localStorage.removeItem(TOKEN_KEY); localStorage.removeItem(USER_KEY); window.location.href = '/'; }

async function apiRequest(url, options = {}) {
    const headers = options.headers || {};
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;
    if (!options.body || options.body instanceof FormData) {
        if (!(options.body instanceof FormData)) delete headers['Content-Type'];
    } else {
        headers['Content-Type'] = 'application/json';
    }
    const response = await fetch(API + url, { ...options, headers });
    if (response.status === 401) {
        logout();
        return null;
    }
    return response;
}

function mostrarMensagem(texto, tipo) {
    const div = document.getElementById('mensagem');
    if (!div) return;
    div.textContent = texto;
    div.className = tipo || 'sucesso';
    setTimeout(() => { div.textContent = ''; div.className = ''; }, 4000);
}

function loginRedirect(userData) {
    if (userData.nivel === 1) window.location.href = '/admin.html';
    else window.location.href = '/cidadao.html';
}

/* ==================== INDEX ==================== */
function initIndex() {
    const loginForm = document.getElementById('login-form');
    const cadastroForm = document.getElementById('cadastro-form');
    const linkCadastro = document.getElementById('link-cadastro');
    const linkLogin = document.getElementById('link-login');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('login-email').value;
            const senha = document.getElementById('login-senha').value;
            try {
                const res = await apiRequest('/acesso/logar', {
                    method: 'POST',
                    body: JSON.stringify({ email, senha })
                });
                if (!res) return;
                if (res.ok) {
                    const data = await res.json();
                    setToken(data.token);
                    setUser(data.usuario);
                    loginRedirect(data.usuario);
                } else {
                    const err = await res.json();
                    mostrarMensagem(err.erro || 'Erro ao fazer login', 'erro');
                }
            } catch (err) { mostrarMensagem('Erro de conexão', 'erro'); }
        });
    }

    if (cadastroForm) {
        cadastroForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const cpf = document.getElementById('cadastro-cpf').value;
            const email = document.getElementById('cadastro-email').value;
            const senha = document.getElementById('cadastro-senha').value;
            try {
                const res = await apiRequest('/acesso/cadastrar', {
                    method: 'POST',
                    body: JSON.stringify({ cpf, email, senha })
                });
                if (res.ok) {
                    mostrarMensagem('Cadastro realizado com sucesso! Faça login.', 'sucesso');
                    document.getElementById('cadastro-section').style.display = 'none';
                    document.getElementById('login-section').style.display = 'block';
                    cadastroForm.reset();
                } else {
                    const err = await res.json();
                    mostrarMensagem(err.message || 'Erro ao cadastrar', 'erro');
                }
            } catch (err) { mostrarMensagem('Erro de conexão', 'erro'); }
        });
    }

    if (linkCadastro) {
        linkCadastro.addEventListener('click', (e) => {
            e.preventDefault();
            document.getElementById('login-section').style.display = 'none';
            document.getElementById('cadastro-section').style.display = 'block';
        });
    }

    if (linkLogin) {
        linkLogin.addEventListener('click', (e) => {
            e.preventDefault();
            document.getElementById('cadastro-section').style.display = 'none';
            document.getElementById('login-section').style.display = 'block';
        });
    }

    if (getToken() && getUser()) loginRedirect(getUser());
}

/* ==================== GERAL (nav + logout) ==================== */
function initNav() {
    const logoutBtn = document.getElementById('btn-logout');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);

    const userInfo = document.getElementById('user-info');
    const user = getUser();
    if (userInfo && user) userInfo.textContent = `Logado como: ${user.email}`;

    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            document.querySelectorAll('main > section').forEach(s => s.style.display = 'none');
            const section = document.getElementById(btn.dataset.section);
            if (section) section.style.display = 'block';
        });
    });
}

function checkAuth() {
    if (!getToken() || !getUser()) window.location.href = '/';
}

/* ==================== CIDADAO ==================== */
async function loadSelects() {
    try {
        const [orgRes, tipRes] = await Promise.all([
            apiRequest('/cidadao/orgao/list'),
            apiRequest('/cidadao/tipo/list')
        ]);
        if (orgRes.ok) {
            const orgaos = await orgRes.json();
            const sel = document.getElementById('denuncia-orgao');
            sel.innerHTML = '<option value="">Selecione...</option>';
            orgaos.forEach(o => sel.innerHTML += `<option value="${o.id}">${o.nome}</option>`);
        }
        if (tipRes.ok) {
            const tipos = await tipRes.json();
            const sel = document.getElementById('denuncia-tipo');
            sel.innerHTML = '<option value="">Selecione...</option>';
            tipos.forEach(t => sel.innerHTML += `<option value="${t.id}">${t.nome}</option>`);
        }
    } catch (err) { mostrarMensagem('Erro ao carregar dados', 'erro'); }
}

function initCidadao() {
    checkAuth();
    initNav();
    loadSelects();
    carregarMinhasDenuncias();

    const form = document.getElementById('denuncia-form');
    const user = getUser();

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData();
            const dados = {
                titulo: document.getElementById('denuncia-titulo').value,
                texto: document.getElementById('denuncia-texto').value,
                urgencia: parseInt(document.getElementById('denuncia-urgencia').value),
                org_id: parseInt(document.getElementById('denuncia-orgao').value),
                tip_id: parseInt(document.getElementById('denuncia-tipo').value),
                usu_id: user.id
            };
            formData.append('dados', new Blob([JSON.stringify(dados)], { type: 'application/json' }));
            const fotos = document.getElementById('denuncia-fotos').files;
            for (let i = 0; i < fotos.length; i++) formData.append('fotos', fotos[i]);

            try {
                const res = await apiRequest('/cidadao/denuncia', { method: 'POST', body: formData });
                if (res.ok) {
                    mostrarMensagem('Denúncia enviada com sucesso!', 'sucesso');
                    form.reset();
                    carregarMinhasDenuncias();
                } else {
                    const err = await res.json();
                    mostrarMensagem(err.message || 'Erro ao enviar denúncia', 'erro');
                }
            } catch (err) { mostrarMensagem('Erro de conexão', 'erro'); }
        });
    }
}

async function carregarMinhasDenuncias() {
    const div = document.getElementById('denuncias-lista');
    if (!div) return;
    div.innerHTML = '<p>Carregando...</p>';
    try {
        const res = await apiRequest('/cidadao/denuncia/list');
        if (res.ok) {
            const denuncias = await res.json();
            if (denuncias.length === 0) { div.innerHTML = '<p>Nenhuma denúncia encontrada.</p>'; return; }
            let html = '<table><thead><tr><th>Título</th><th>Data</th><th>Urgência</th><th>Órgão</th><th>Tipo</th><th>Ações</th></tr></thead><tbody>';
            denuncias.forEach(d => {
                html += `<tr>
                    <td>${d.titulo}</td>
                    <td>${d.data || '-'}</td>
                    <td>${d.urgencia}</td>
                    <td>${d.orgao ? d.orgao.nome : '-'}</td>
                    <td>${d.tipo ? d.tipo.nome : '-'}</td>
                    <td><button class="btn-feedback" onclick="verFeedbackCidadao(${d.id})">Ver Feedback</button></td>
                </tr>`;
            });
            html += '</tbody></table>';
            div.innerHTML = html;
        }
    } catch (err) { div.innerHTML = '<p>Erro ao carregar denúncias.</p>'; }
}

async function verFeedbackCidadao(id) {
    document.querySelectorAll('main > section').forEach(s => s.style.display = 'none');
    const section = document.getElementById('feedback-section');
    section.style.display = 'block';
    document.getElementById('feedback-conteudo').innerHTML = '<p>Carregando...</p>';
    document.getElementById('btn-voltar-feedback').onclick = () => {
        section.style.display = 'none';
        document.getElementById('minhas-denuncias-section').style.display = 'block';
    };
    try {
        const res = await apiRequest(`/cidadao/denuncia/${id}/feedback`);
        const texto = await res.text();
        document.getElementById('feedback-conteudo').innerHTML = `<p>${texto}</p>`;
    } catch (err) {
        document.getElementById('feedback-conteudo').innerHTML = '<p>Erro ao carregar feedback.</p>';
    }
}

window.verFeedbackCidadao = verFeedbackCidadao;

/* ==================== ADMIN ==================== */

/* ---- ORGAOS ---- */
async function carregarOrgaos() {
    const div = document.getElementById('orgaos-lista');
    div.innerHTML = '<p>Carregando...</p>';
    try {
        const res = await apiRequest('/adm/orgao/list');
        if (res.ok) {
            const orgaos = await res.json();
            if (orgaos.length === 0) { div.innerHTML = '<p>Nenhum órgão cadastrado.</p>'; return; }
            let html = '<table><thead><tr><th>ID</th><th>Nome</th><th>Ações</th></tr></thead><tbody>';
            orgaos.forEach(o => {
                html += `<tr>
                    <td>${o.id}</td>
                    <td>${o.nome}</td>
                    <td>
                        <button class="btn-editar" onclick="editarOrgao(${o.id},'${o.nome.replace(/'/g, "\\'")}')">Editar</button>
                        <button class="btn-excluir" onclick="deletarOrgao(${o.id})">Excluir</button>
                    </td>
                </tr>`;
            });
            html += '</tbody></table>';
            div.innerHTML = html;
        }
    } catch (err) { div.innerHTML = '<p>Erro ao carregar órgãos.</p>'; }
}

function editarOrgao(id, nome) {
    document.getElementById('orgao-id').value = id;
    document.getElementById('orgao-nome').value = nome;
    document.getElementById('orgao-submit').textContent = 'Atualizar';
    document.getElementById('orgao-cancelar').style.display = 'inline';
}

async function deletarOrgao(id) {
    if (!confirm('Excluir este órgão?')) return;
    try {
        const res = await apiRequest(`/adm/orgao/${id}`, { method: 'DELETE' });
        if (res.ok) { mostrarMensagem('Órgão excluído.', 'sucesso'); carregarOrgaos(); }
        else mostrarMensagem('Erro ao excluir.', 'erro');
    } catch (err) { mostrarMensagem('Erro de conexão', 'erro'); }
}

/* ---- TIPOS ---- */
async function carregarTipos() {
    const div = document.getElementById('tipos-lista');
    div.innerHTML = '<p>Carregando...</p>';
    try {
        const res = await apiRequest('/adm/tipo/list');
        if (res.ok) {
            const tipos = await res.json();
            if (tipos.length === 0) { div.innerHTML = '<p>Nenhum tipo cadastrado.</p>'; return; }
            let html = '<table><thead><tr><th>ID</th><th>Nome</th><th>Ações</th></tr></thead><tbody>';
            tipos.forEach(t => {
                html += `<tr>
                    <td>${t.id}</td>
                    <td>${t.nome}</td>
                    <td>
                        <button class="btn-editar" onclick="editarTipo(${t.id},'${t.nome.replace(/'/g, "\\'")}')">Editar</button>
                        <button class="btn-excluir" onclick="deletarTipo(${t.id})">Excluir</button>
                    </td>
                </tr>`;
            });
            html += '</tbody></table>';
            div.innerHTML = html;
        }
    } catch (err) { div.innerHTML = '<p>Erro ao carregar tipos.</p>'; }
}

function editarTipo(id, nome) {
    document.getElementById('tipo-id').value = id;
    document.getElementById('tipo-nome').value = nome;
    document.getElementById('tipo-submit').textContent = 'Atualizar';
    document.getElementById('tipo-cancelar').style.display = 'inline';
}

async function deletarTipo(id) {
    if (!confirm('Excluir este tipo?')) return;
    try {
        const res = await apiRequest(`/adm/tipo/${id}`, { method: 'DELETE' });
        if (res.ok) { mostrarMensagem('Tipo excluído.', 'sucesso'); carregarTipos(); }
        else mostrarMensagem('Erro ao excluir.', 'erro');
    } catch (err) { mostrarMensagem('Erro de conexão', 'erro'); }
}

/* ---- DENUNCIAS (ADMIN) ---- */
async function carregarDenunciasAdmin(titulo) {
    const div = document.getElementById('denuncias-lista');
    if (!div) return;
    div.innerHTML = '<p>Carregando...</p>';
    try {
        let url = '/adm/denuncia/list';
        if (titulo) url += `?titulo=${encodeURIComponent(titulo)}`;
        const res = await apiRequest(url);
        if (res.ok) {
            const denuncias = await res.json();
            if (denuncias.length === 0) { div.innerHTML = '<p>Nenhuma denúncia encontrada.</p>'; return; }
            let html = '<table><thead><tr><th>ID</th><th>Título</th><th>Usuário</th><th>Data</th><th>Urgência</th><th>Ações</th></tr></thead><tbody>';
            denuncias.forEach(d => {
                const usuario = d.usuario ? d.usuario.email : '-';
                html += `<tr>
                    <td>${d.id}</td>
                    <td>${d.titulo}</td>
                    <td>${usuario}</td>
                    <td>${d.data || '-'}</td>
                    <td>${d.urgencia}</td>
                    <td>
                        <button class="btn-feedback" onclick="abrirFeedbackAdmin(${d.id},'${d.titulo.replace(/'/g, "\\'")}')">Feedback</button>
                        <button class="btn-excluir" onclick="deletarDenunciaAdmin(${d.id})">Excluir</button>
                    </td>
                </tr>`;
            });
            html += '</tbody></table>';
            div.innerHTML = html;
        }
    } catch (err) { div.innerHTML = '<p>Erro ao carregar denúncias.</p>'; }
}

async function deletarDenunciaAdmin(id) {
    if (!confirm('Excluir esta denúncia permanentemente?')) return;
    try {
        const res = await apiRequest(`/adm/denuncia/${id}`, { method: 'DELETE' });
        if (res.ok) { mostrarMensagem('Denúncia excluída.', 'sucesso'); carregarDenunciasAdmin(document.getElementById('filtro-titulo').value); }
        else mostrarMensagem('Erro ao excluir.', 'erro');
    } catch (err) { mostrarMensagem('Erro de conexão', 'erro'); }
}

function abrirFeedbackAdmin(id, titulo) {
    document.querySelectorAll('main > section').forEach(s => s.style.display = 'none');
    document.getElementById('feedback-section').style.display = 'block';
    document.getElementById('feedback-denuncia-id').value = id;
    document.getElementById('feedback-denuncia-info').textContent = `Denúncia #${id}: ${titulo}`;
}

function initAdmin() {
    checkAuth();
    initNav();
    carregarOrgaos();
    carregarTipos();
    carregarDenunciasAdmin();

    /* Orgao form */
    const orgaoForm = document.getElementById('orgao-form');
    if (orgaoForm) {
        orgaoForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const id = document.getElementById('orgao-id').value;
            const nome = document.getElementById('orgao-nome').value;
            const isUpdate = id && id !== '0';
            try {
                let res;
                if (isUpdate) {
                    res = await apiRequest(`/adm/orgao/${id}`, { method: 'PUT', body: JSON.stringify({ nome }) });
                } else {
                    res = await apiRequest('/adm/orgao', { method: 'POST', body: JSON.stringify({ nome }) });
                }
                if (res.ok) {
                    mostrarMensagem(isUpdate ? 'Órgão atualizado.' : 'Órgão criado.', 'sucesso');
                    orgaoForm.reset();
                    document.getElementById('orgao-id').value = '';
                    document.getElementById('orgao-submit').textContent = 'Salvar';
                    document.getElementById('orgao-cancelar').style.display = 'none';
                    carregarOrgaos();
                } else mostrarMensagem('Erro ao salvar.', 'erro');
            } catch (err) { mostrarMensagem('Erro de conexão', 'erro'); }
        });
    }

    document.getElementById('orgao-cancelar')?.addEventListener('click', () => {
        document.getElementById('orgao-form').reset();
        document.getElementById('orgao-id').value = '';
        document.getElementById('orgao-submit').textContent = 'Salvar';
        document.getElementById('orgao-cancelar').style.display = 'none';
    });

    /* Tipo form */
    const tipoForm = document.getElementById('tipo-form');
    if (tipoForm) {
        tipoForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const id = document.getElementById('tipo-id').value;
            const nome = document.getElementById('tipo-nome').value;
            const isUpdate = id && id !== '0';
            try {
                let res;
                if (isUpdate) {
                    res = await apiRequest(`/adm/tipo/${id}`, { method: 'PUT', body: JSON.stringify({ nome }) });
                } else {
                    res = await apiRequest('/adm/tipo', { method: 'POST', body: JSON.stringify({ nome }) });
                }
                if (res.ok) {
                    mostrarMensagem(isUpdate ? 'Tipo atualizado.' : 'Tipo criado.', 'sucesso');
                    tipoForm.reset();
                    document.getElementById('tipo-id').value = '';
                    document.getElementById('tipo-submit').textContent = 'Salvar';
                    document.getElementById('tipo-cancelar').style.display = 'none';
                    carregarTipos();
                } else mostrarMensagem('Erro ao salvar.', 'erro');
            } catch (err) { mostrarMensagem('Erro de conexão', 'erro'); }
        });
    }

    document.getElementById('tipo-cancelar')?.addEventListener('click', () => {
        document.getElementById('tipo-form').reset();
        document.getElementById('tipo-id').value = '';
        document.getElementById('tipo-submit').textContent = 'Salvar';
        document.getElementById('tipo-cancelar').style.display = 'none';
    });

    /* Feedback form */
    const feedbackForm = document.getElementById('feedback-form');
    if (feedbackForm) {
        feedbackForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const id = document.getElementById('feedback-denuncia-id').value;
            const texto = document.getElementById('feedback-texto').value;
            try {
                const res = await apiRequest(`/adm/denuncia/${id}/feedback`, { method: 'POST', body: JSON.stringify({ texto }) });
                if (res.ok) {
                    mostrarMensagem('Feedback registrado!', 'sucesso');
                    feedbackForm.reset();
                    document.getElementById('feedback-section').style.display = 'none';
                    document.getElementById('denuncias-section').style.display = 'block';
                    carregarDenunciasAdmin(document.getElementById('filtro-titulo').value);
                } else {
                    const err = await res.json();
                    mostrarMensagem(err.message || 'Erro ao registrar feedback', 'erro');
                }
            } catch (err) { mostrarMensagem('Erro de conexão', 'erro'); }
        });
    }

    document.getElementById('feedback-cancelar')?.addEventListener('click', () => {
        document.getElementById('feedback-section').style.display = 'none';
        document.getElementById('denuncias-section').style.display = 'block';
    });

    /* Filtro denuncias */
    document.getElementById('btn-filtrar')?.addEventListener('click', () => {
        carregarDenunciasAdmin(document.getElementById('filtro-titulo').value);
    });
}

window.editarOrgao = editarOrgao;
window.deletarOrgao = deletarOrgao;
window.editarTipo = editarTipo;
window.deletarTipo = deletarTipo;
window.abrirFeedbackAdmin = abrirFeedbackAdmin;
window.deletarDenunciaAdmin = deletarDenunciaAdmin;

/* ==================== INIT ==================== */
document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname;
    if (path === '/' || path.endsWith('index.html')) initIndex();
    else if (path.endsWith('cidadao.html')) initCidadao();
    else if (path.endsWith('admin.html')) initAdmin();
});
