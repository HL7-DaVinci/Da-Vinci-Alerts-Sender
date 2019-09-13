		Vue.use(VueRouter);
		ELEMENT.locale(ELEMENT.lang.en);

		function qs(query = {}) {
			return Object.entries(query).map(q => q[1] ? q.join("=") : null).filter(Boolean).join("&");
		}
		function errorMessage(data) {
			return data ? 'Error: ' + data : 'Error happened.';
		}


		var hlxPatientForm = Vue.component("hlx-patient-form", {
			template: "#hlx-patient-form",
			data() {
				return {
                    eventSource : undefined,
                    messages: [],
                    collapseMessages: []
				};
			},
			created(){
			    this.eventSource = new EventSource(`/subscribe`);
			    this.eventSource.addEventListener("message", ({ data }) => {
                    this.messages.unshift( JSON.parse(data) );
                    this.$message.success("Message received");
                });

                window.addEventListener('beforeunload', this.leaving);
			},
			methods: {
				leaving() {
                    console.log("unsubscribe");
				}
			}
		});

		var router = new VueRouter({
			mode: "history",
			routes: [{
				path: "*",
				component: hlxPatientForm
			}]
		});

		var app = new Vue({
			el: "#app",
			router
		});