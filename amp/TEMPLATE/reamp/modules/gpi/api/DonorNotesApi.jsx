import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
class DonorNotesApi {    
    static save(data) { 
        const url = Array.isArray(data) ? '/rest/gpi/donor-notes/save-all' : '/rest/gpi/donor-notes';        
        return new Promise((resolve, reject) => {
            postJson(url, data).then((response) => {
                resolve(response.json())
            }).catch((error) => {
                reject(error);
            });
        });
    }
    
    static getDonorNotesList(data) { 
        const url = '/rest/gpi/donor-notes?offset=' + data.paging.offset + '&count=' + data.paging.recordsPerPage + '&orderby=' + data.sorting.orderBy + '&sort=' + data.sorting.sortOrder;    
        return new Promise((resolve, reject) => {
            fetchJson(url).then((response) => {
                resolve(response)
            }).catch((error) => {
                reject(error);
            });
        });
    }
    
    static deleteDonorNotes(donorNotes) {          
        return new Promise((resolve, reject) => {
            deleteJson('/rest/gpi/donor-notes/' + donorNotes.id, {}).then(response => {
                resolve(response.json());
            }).catch(error => {
                reject(error);
            });
        });          
    }
    
}

export default DonorNotesApi;