// Token armazenado após login
let token = null;

// Utilitário para chamadas autenticadas
async function apiRequest(url, options = {}) {
    const headers = options.headers || {};
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    const response = await fetch(url, { ...options, headers });
    return response;
}
