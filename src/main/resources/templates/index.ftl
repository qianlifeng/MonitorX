<#import "layout.ftl" as layout />

<@layout.masterTemplate title="Home" initScript="js/index">
<div class="index">
    <h1>Server List</h1>
    <table class="table table-bordered">
        <tr>
            <th>
                Name
            </th>
            <th>
                Status
            </th>
            <th>
                Last Update
            </th>
        </tr>
        <tr v-for="node in nodes" v-bind:class="{'success': node.status.status == 'up','danger': node.status.status == 'down'}">
            <td>
                <a href="/d/?node={{node.code}}" v-text="node.title"></a>
            </td>
            <td v-text="node.status.status">
            </td>
            <td v-text="node.status.formattedLastUpdateDate">
            </td>
        </tr>
    </table>
</div>
</@layout.masterTemplate>