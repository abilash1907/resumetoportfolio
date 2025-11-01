export const styles = {
  container: {
    fontFamily: 'Segoe UI, sans-serif', background: '#f8faff', minHeight: '100vh'
  },
  header: {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    padding: '10px', background: '#fff', borderBottom: '1px solid rgb(35, 69, 103)'
  },
  title: { fontSize: '1.5rem', fontWeight: 'bold', color: '#234567' },
  deployBtn: {
    background: '#001f53', color: 'white', border: 'none', padding: '12px 32px',
    fontSize: '1rem', borderRadius: '8px', cursor: 'pointer', fontWeight: '600'
  },
  disableDeployBtn: {
    background: '#f5f9ff', color: '#889ab1', border: 'none', padding: '12px 32px',
    fontSize: '1rem', borderRadius: '8px', cursor: 'pointer', fontWeight: '600'
  },
  uploadSection: {
    background: '#fff', margin: '30px', padding: '24px', borderRadius: '16px', 
    boxShadow: '0 2px 12px rgba(0,0,0,0.1)'
  },
  uploadTitle: { margin: 0, fontSize: '1.2rem', color: 'rgb(35, 69, 103)', fontWeight: '600' },
  fileInput: { marginRight: '16px', marginTop: '10px' },
  uploadBtn: {
    background: '#ebf3ff', color: 'rgb(35, 69, 103)', border: 'none',
    padding: '8px 24px', borderRadius: '8px', fontWeight: '500', marginLeft: '8px', cursor: 'pointer'
  },
  fileName: {
    marginLeft: '24px', color: '#789'
  },
  mainSection: {
    display: 'flex', gap: '24px', margin: '32px'
  },
  codeEditor: {
    background: '#fff', flex: 1, padding: '24px', borderRadius: '16px',
    boxShadow: '0 2px 12px rgba(0,0,0,0.03)'
  },
  textArea: {
    width: '100%', margin: '0px', borderRadius: '8px',
    fontFamily: 'monospace', fontSize: '1rem', borderColor: '#dde', height:'100vh'
  },
  preview: {
    background: '#fff',display:'flex', justifyContent:'space-between', flex: 1, padding: '24px', borderRadius: '16px',
    boxShadow: '0 2px 12px rgba(0,0,0,0.03)'
  },
  iframe: {
    width: '100%', height: '100vh', border: '1px solid #dde', borderRadius: '7px'
  }
};